package com.fuelmanagement.airport.web;

import com.fuelmanagement.airport.application.AirportImportRow;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ImportAirportsRequest(
        @NotBlank String batchId,
        @NotNull @NotEmpty List<AirportImportRow> airports
) {}
