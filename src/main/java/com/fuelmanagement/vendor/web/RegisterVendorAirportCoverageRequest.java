package com.fuelmanagement.vendor.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterVendorAirportCoverageRequest(
        @NotBlank @Size(max = 4) String icaoCode,
        @NotBlank @Size(max = 20) String fuelTypeCode,
        @Size(max = 50) String intoPlaneAgentCode,
        String notes
) {}
