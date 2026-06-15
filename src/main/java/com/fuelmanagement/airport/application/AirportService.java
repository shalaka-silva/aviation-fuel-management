package com.fuelmanagement.airport.application;

import com.fuelmanagement.airport.domain.Airport;
import com.fuelmanagement.airport.domain.AirportNotFoundException;
import com.fuelmanagement.airport.domain.DuplicateAirportException;
import com.fuelmanagement.airport.infrastructure.AirportRepository;
import com.fuelmanagement.airport.web.AirportResponse;
import com.fuelmanagement.airport.web.CreateAirportRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public AirportResponse createAirport(CreateAirportRequest request) {
        if (airportRepository.existsByIcaoCode(request.icaoCode())) {
            throw new DuplicateAirportException("Airport already exists with ICAO code: " + request.icaoCode());
        }
        Airport airport = new Airport();
        airport.setIcaoCode(request.icaoCode());
        airport.setIataCode(request.iataCode());
        airport.setName(request.name());
        airport.setCity(request.city());
        airport.setCountry(request.country());
        airport.setLatitude(request.latitude());
        airport.setLongitude(request.longitude());
        airport.setTimezone(request.timezone());
        airport.setElevation(request.elevation());
        airport.setActive(true);
        return AirportResponse.from(airportRepository.save(airport));
    }

    @Transactional(readOnly = true)
    public AirportResponse getByIcaoCode(String icaoCode) {
        return airportRepository.findByIcaoCode(icaoCode)
                .map(AirportResponse::from)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found: " + icaoCode));
    }

    @Transactional(readOnly = true)
    public Page<AirportResponse> listAirports(Pageable pageable, boolean includeInactive) {
        if (includeInactive) {
            return airportRepository.findAll(pageable).map(AirportResponse::from);
        }
        return airportRepository.findAllByActiveTrue(pageable).map(AirportResponse::from);
    }

    public AirportResponse deactivateAirport(String icaoCode) {
        Airport airport = airportRepository.findByIcaoCode(icaoCode)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found: " + icaoCode));
        airport.setActive(false);
        return AirportResponse.from(airportRepository.save(airport));
    }
}
