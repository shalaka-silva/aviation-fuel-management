package com.fuelmanagement.airport.application;

import com.fuelmanagement.airport.domain.Airport;
import com.fuelmanagement.airport.infrastructure.AirportImportRecord;
import com.fuelmanagement.airport.infrastructure.AirportImportRepository;
import com.fuelmanagement.airport.infrastructure.AirportRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AirportImportService {

    private final AirportRepository airportRepository;
    private final AirportImportRepository importRepository;
    private final Validator validator;

    public AirportImportService(AirportRepository airportRepository,
                                 AirportImportRepository importRepository,
                                 Validator validator) {
        this.airportRepository = airportRepository;
        this.importRepository = importRepository;
        this.validator = validator;
    }

    public AirportImportResult importAirports(String batchId, List<AirportImportRow> rows) {
        List<AirportImportResult.AirportImportRowError> errors = new ArrayList<>();
        int successCount = 0;

        for (int i = 0; i < rows.size(); i++) {
            AirportImportRow row = rows.get(i);
            Set<ConstraintViolation<AirportImportRow>> violations = validator.validate(row);

            if (!violations.isEmpty()) {
                List<String> messages = violations.stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .sorted()
                        .collect(Collectors.toList());
                importRepository.save(AirportImportRecord.failed(
                        batchId, row.icaoCode(), row.sourceData(), String.join("; ", messages)));
                errors.add(new AirportImportResult.AirportImportRowError(i, row.icaoCode(), messages));
                continue;
            }

            if (airportRepository.existsByIcaoCode(row.icaoCode())) {
                String message = "Duplicate ICAO code: " + row.icaoCode();
                importRepository.save(AirportImportRecord.failed(
                        batchId, row.icaoCode(), row.sourceData(), message));
                errors.add(new AirportImportResult.AirportImportRowError(i, row.icaoCode(), List.of(message)));
                continue;
            }

            Airport airport = new Airport();
            airport.setIcaoCode(row.icaoCode());
            airport.setIataCode(row.iataCode());
            airport.setName(row.name());
            airport.setCity(row.city());
            airport.setCountry(row.country());
            airport.setLatitude(row.latitude());
            airport.setLongitude(row.longitude());
            airport.setTimezone(row.timezone());
            airport.setElevation(row.elevation());
            airport.setActive(true);
            Airport saved = airportRepository.save(airport);

            importRepository.save(AirportImportRecord.success(
                    batchId, row.icaoCode(), row.sourceData(), saved.getId()));
            successCount++;
        }

        return new AirportImportResult(rows.size(), successCount, errors.size(), errors);
    }
}
