package com.fuelmanagement.airport.infrastructure;

import com.fuelmanagement.TestcontainersConfiguration;
import com.fuelmanagement.airport.domain.Airport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AirportRepositoryTest {

    @Autowired
    private AirportRepository airportRepository;

    private Airport buildAirport(String icaoCode, String iataCode, boolean active) {
        Airport a = new Airport();
        a.setIcaoCode(icaoCode);
        a.setIataCode(iataCode);
        a.setName("Test Airport " + icaoCode);
        a.setCity("Test City");
        a.setCountry("AE");
        a.setLatitude(new BigDecimal("25.252800"));
        a.setLongitude(new BigDecimal("55.364400"));
        a.setTimezone("Asia/Dubai");
        a.setElevation(19);
        a.setActive(active);
        return a;
    }

    @Test
    void save_and_findByIcaoCode_returnsCorrectData() {
        airportRepository.save(buildAirport("OMDB", "DXB", true));

        Optional<Airport> found = airportRepository.findByIcaoCode("OMDB");

        assertThat(found).isPresent();
        assertThat(found.get().getIataCode()).isEqualTo("DXB");
        assertThat(found.get().getLatitude()).isEqualByComparingTo(new BigDecimal("25.252800"));
    }

    @Test
    void existsByIcaoCode_existingCode_returnsTrue() {
        airportRepository.save(buildAirport("OMDB", null, true));
        assertThat(airportRepository.existsByIcaoCode("OMDB")).isTrue();
    }

    @Test
    void existsByIcaoCode_absentCode_returnsFalse() {
        assertThat(airportRepository.existsByIcaoCode("ZZZZ")).isFalse();
    }

    @Test
    void duplicateIcaoCode_throwsDataIntegrityViolation() {
        airportRepository.saveAndFlush(buildAirport("OMDB", "DXB", true));

        assertThatThrownBy(() -> airportRepository.saveAndFlush(buildAirport("OMDB", null, true)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findAllByActiveTrue_returnsOnlyActiveAirports() {
        airportRepository.save(buildAirport("OMDB", "DXB", true));
        airportRepository.save(buildAirport("EGLL", "LHR", false));

        Page<Airport> page = airportRepository.findAllByActiveTrue(PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getIcaoCode()).isEqualTo("OMDB");
    }

    @Test
    void findAll_paged_returnsCorrectPage() {
        airportRepository.save(buildAirport("OMDB", "DXB", true));
        airportRepository.save(buildAirport("EGLL", "LHR", false));
        airportRepository.save(buildAirport("KJFK", "JFK", true));

        Page<Airport> page = airportRepository.findAll(PageRequest.of(0, 2));

        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getContent()).hasSize(2);
    }

    @Test
    void findByIataCode_returnsCorrectAirport() {
        airportRepository.save(buildAirport("OMDB", "DXB", true));
        airportRepository.save(buildAirport("EGLL", "LHR", true));

        Optional<Airport> found = airportRepository.findByIataCode("LHR");

        assertThat(found).isPresent();
        assertThat(found.get().getIcaoCode()).isEqualTo("EGLL");
    }
}
