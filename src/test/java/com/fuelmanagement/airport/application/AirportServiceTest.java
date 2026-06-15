package com.fuelmanagement.airport.application;

import com.fuelmanagement.airport.domain.Airport;
import com.fuelmanagement.airport.domain.AirportNotFoundException;
import com.fuelmanagement.airport.domain.DuplicateAirportException;
import com.fuelmanagement.airport.infrastructure.AirportRepository;
import com.fuelmanagement.airport.web.AirportResponse;
import com.fuelmanagement.airport.web.CreateAirportRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @InjectMocks
    private AirportService airportService;

    private CreateAirportRequest validRequest() {
        return new CreateAirportRequest("OMDB", "DXB", "Dubai Intl", "Dubai", "AE",
                BigDecimal.valueOf(25.2528), BigDecimal.valueOf(55.3644), "Asia/Dubai", 19);
    }

    private Airport airportEntity(String icaoCode) {
        Airport a = new Airport();
        a.setIcaoCode(icaoCode);
        a.setIataCode("DXB");
        a.setName("Dubai Intl");
        a.setCity("Dubai");
        a.setCountry("AE");
        a.setLatitude(BigDecimal.valueOf(25.2528));
        a.setLongitude(BigDecimal.valueOf(55.3644));
        a.setTimezone("Asia/Dubai");
        a.setElevation(19);
        a.setActive(true);
        return a;
    }

    @Test
    void createAirport_success_returnsResponse() {
        when(airportRepository.existsByIcaoCode("OMDB")).thenReturn(false);
        when(airportRepository.save(any())).thenReturn(airportEntity("OMDB"));

        AirportResponse response = airportService.createAirport(validRequest());

        assertThat(response.icaoCode()).isEqualTo("OMDB");
        assertThat(response.iataCode()).isEqualTo("DXB");
        assertThat(response.active()).isTrue();
        verify(airportRepository).save(any());
    }

    @Test
    void createAirport_duplicateIcao_throwsDuplicateException() {
        when(airportRepository.existsByIcaoCode("OMDB")).thenReturn(true);

        assertThatThrownBy(() -> airportService.createAirport(validRequest()))
                .isInstanceOf(DuplicateAirportException.class)
                .hasMessageContaining("OMDB");

        verify(airportRepository, never()).save(any());
    }

    @Test
    void getByIcaoCode_found_returnsResponse() {
        when(airportRepository.findByIcaoCode("OMDB")).thenReturn(Optional.of(airportEntity("OMDB")));

        AirportResponse response = airportService.getByIcaoCode("OMDB");

        assertThat(response.icaoCode()).isEqualTo("OMDB");
    }

    @Test
    void getByIcaoCode_notFound_throwsNotFoundException() {
        when(airportRepository.findByIcaoCode("ZZZZ")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> airportService.getByIcaoCode("ZZZZ"))
                .isInstanceOf(AirportNotFoundException.class)
                .hasMessageContaining("ZZZZ");
    }

    @Test
    void listAirports_activeOnly_usesActiveTrueQuery() {
        when(airportRepository.findAllByActiveTrue(any()))
                .thenReturn(new PageImpl<>(List.of(airportEntity("OMDB"))));

        var page = airportService.listAirports(PageRequest.of(0, 10), false);

        assertThat(page.getContent()).hasSize(1);
        verify(airportRepository).findAllByActiveTrue(any());
        verify(airportRepository, never()).findAll(any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void listAirports_includeInactive_usesFullQuery() {
        when(airportRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(airportEntity("OMDB"))));

        var page = airportService.listAirports(PageRequest.of(0, 10), true);

        assertThat(page.getContent()).hasSize(1);
        verify(airportRepository).findAll(any(org.springframework.data.domain.Pageable.class));
        verify(airportRepository, never()).findAllByActiveTrue(any());
    }

    @Test
    void deactivateAirport_found_setsActiveFalse() {
        Airport airport = airportEntity("OMDB");
        when(airportRepository.findByIcaoCode("OMDB")).thenReturn(Optional.of(airport));
        when(airportRepository.save(airport)).thenReturn(airport);

        AirportResponse response = airportService.deactivateAirport("OMDB");

        assertThat(airport.isActive()).isFalse();
        assertThat(response.active()).isFalse();
        verify(airportRepository).save(airport);
    }

    @Test
    void deactivateAirport_notFound_throwsNotFoundException() {
        when(airportRepository.findByIcaoCode("ZZZZ")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> airportService.deactivateAirport("ZZZZ"))
                .isInstanceOf(AirportNotFoundException.class);
    }
}
