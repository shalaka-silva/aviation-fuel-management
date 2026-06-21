package com.fuelmanagement.fuel.web;

import com.fuelmanagement.fuel.domain.UnitOfMeasure;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateFuelTypeRequest(
        @NotBlank @Size(max = 20) String code,
        @NotBlank @Size(max = 100) String name,
        @NotNull UnitOfMeasure defaultUnitOfMeasure
) {}
