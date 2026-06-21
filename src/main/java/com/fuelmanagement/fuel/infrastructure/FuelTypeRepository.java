package com.fuelmanagement.fuel.infrastructure;

import com.fuelmanagement.fuel.domain.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuelTypeRepository extends JpaRepository<FuelType, Long> {
    boolean existsByCode(String code);
    Optional<FuelType> findByCode(String code);
    List<FuelType> findAllByActiveTrue();
}
