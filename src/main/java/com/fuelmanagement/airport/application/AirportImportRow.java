package com.fuelmanagement.airport.application;

import com.fuelmanagement.common.validation.IataCode;
import com.fuelmanagement.common.validation.IcaoCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AirportImportRow(
        @NotBlank @IcaoCode String icaoCode,
        @IataCode String iataCode,
        @NotBlank String name,
        @NotBlank String city,
        @NotBlank String country,
        @NotNull BigDecimal latitude,
        @NotNull BigDecimal longitude,
        @NotBlank String timezone,
        Integer elevation,
        @NotBlank String sourceData
) {}
