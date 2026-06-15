package com.fuelmanagement.airport.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportImportRepository extends JpaRepository<AirportImportRecord, Long> {
}
