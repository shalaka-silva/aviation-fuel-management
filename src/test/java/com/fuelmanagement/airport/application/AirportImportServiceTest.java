package com.fuelmanagement.airport.application;

import com.fuelmanagement.airport.domain.Airport;
import com.fuelmanagement.airport.infrastructure.AirportImportRecord;
import com.fuelmanagement.airport.infrastructure.AirportImportRepository;
import com.fuelmanagement.airport.infrastructure.AirportRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportImportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @Mock
    private AirportImportRepository importRepository;

    private AirportImportService importService;

    @BeforeEach
    void setup() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        importService = new AirportImportService(airportRepository, importRepository, validator);
    }

    private AirportImportRow validRow(String icaoCode, String iataCode) {
        return new AirportImportRow(icaoCode, iataCode, "Test Airport", "Dubai", "AE",
                BigDecimal.valueOf(25.2528), BigDecimal.valueOf(55.3644), "Asia/Dubai", null,
                "{\"icao\":\"" + icaoCode + "\"}");
    }

    private Airport savedAirport(String icaoCode, Long id) {
        Airport a = new Airport();
        a.setIcaoCode(icaoCode);
        a.setName("Test Airport");
        a.setCity("Dubai");
        a.setCountry("AE");
        a.setLatitude(BigDecimal.valueOf(25.2528));
        a.setLongitude(BigDecimal.valueOf(55.3644));
        a.setTimezone("Asia/Dubai");
        a.setActive(true);
        return a;
    }

    @Test
    void allValidRows_allSucceed() {
        when(airportRepository.existsByIcaoCode(any())).thenReturn(false);
        when(airportRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(importRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AirportImportResult result = importService.importAirports("batch-1",
                List.of(validRow("OMDB", "DXB"), validRow("EGLL", "LHR")));

        assertThat(result.totalRows()).isEqualTo(2);
        assertThat(result.successCount()).isEqualTo(2);
        assertThat(result.failureCount()).isEqualTo(0);
        assertThat(result.errors()).isEmpty();
    }

    @Test
    void invalidIcaoRow_reportedAsError_othersSucceed() {
        when(airportRepository.existsByIcaoCode("EGLL")).thenReturn(false);
        when(airportRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(importRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AirportImportRow badRow = new AirportImportRow("bad", null, "Airport", "City", "AE",
                BigDecimal.ONE, BigDecimal.ONE, "UTC", null, "raw");
        AirportImportRow goodRow = validRow("EGLL", "LHR");

        AirportImportResult result = importService.importAirports("batch-2", List.of(badRow, goodRow));

        assertThat(result.totalRows()).isEqualTo(2);
        assertThat(result.successCount()).isEqualTo(1);
        assertThat(result.failureCount()).isEqualTo(1);
        assertThat(result.errors()).hasSize(1);
        assertThat(result.errors().get(0).rowIndex()).isEqualTo(0);
    }

    @Test
    void duplicateIcao_reportedAsError() {
        when(airportRepository.existsByIcaoCode("OMDB")).thenReturn(true);
        when(importRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AirportImportResult result = importService.importAirports("batch-3", List.of(validRow("OMDB", "DXB")));

        assertThat(result.failureCount()).isEqualTo(1);
        assertThat(result.errors().get(0).messages()).anyMatch(m -> m.contains("Duplicate"));
        verify(airportRepository, never()).save(any());
    }

    @Test
    void allRowsInvalid_noAirportsSaved() {
        when(importRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AirportImportRow bad1 = new AirportImportRow("", null, "", "", "",
                null, null, "", null, "raw1");
        AirportImportRow bad2 = new AirportImportRow("xyz", null, "", "", "",
                null, null, "", null, "raw2");

        AirportImportResult result = importService.importAirports("batch-4", List.of(bad1, bad2));

        assertThat(result.failureCount()).isEqualTo(2);
        assertThat(result.successCount()).isEqualTo(0);
        verify(airportRepository, never()).save(any());
    }

    @Test
    void emptyBatch_returnsZeroCounts() {
        AirportImportResult result = importService.importAirports("batch-5", List.of());

        assertThat(result.totalRows()).isEqualTo(0);
        assertThat(result.successCount()).isEqualTo(0);
        assertThat(result.failureCount()).isEqualTo(0);
    }

    @Test
    void importRecord_sourceDataPreservedForEachRow() {
        when(airportRepository.existsByIcaoCode(any())).thenReturn(false);
        when(airportRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ArgumentCaptor<AirportImportRecord> captor = ArgumentCaptor.forClass(AirportImportRecord.class);
        when(importRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

        importService.importAirports("batch-6", List.of(validRow("OMDB", "DXB")));

        AirportImportRecord saved = captor.getValue();
        assertThat(saved.getSourceData()).contains("OMDB");
        assertThat(saved.getImportStatus()).isEqualTo("SUCCESS");
    }
}
