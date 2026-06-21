package com.fuelmanagement.fuel.web;

import com.fuelmanagement.fuel.domain.FuelType;
import com.fuelmanagement.fuel.domain.UnitOfMeasure;

public record FuelTypeResponse(
        Long id,
        String code,
        String name,
        UnitOfMeasure defaultUnitOfMeasure,
        boolean active
) {
    public static FuelTypeResponse from(FuelType fuelType) {
        return new FuelTypeResponse(
                fuelType.getId(),
                fuelType.getCode(),
                fuelType.getName(),
                fuelType.getDefaultUnitOfMeasure(),
                fuelType.isActive()
        );
    }
}
