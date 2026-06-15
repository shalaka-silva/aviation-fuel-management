package com.fuelmanagement.airport.infrastructure;

import com.fuelmanagement.airport.domain.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {

    Optional<Airport> findByIcaoCode(String icaoCode);

    boolean existsByIcaoCode(String icaoCode);

    Optional<Airport> findByIataCode(String iataCode);

    Page<Airport> findAllByActiveTrue(Pageable pageable);
}
