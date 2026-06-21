package com.fuelmanagement.vendor.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateIntoPlaneAgentRequest(
        @NotBlank @Size(max = 50) String code,
        @NotBlank @Size(max = 200) String name,
        @Size(max = 100) String operatingHours,
        @Size(max = 100) String minimumNoticeRequired,
        @Size(max = 300) String rampsServiced,
        @Size(max = 200) String contactEmail,
        @Size(max = 50) String contactPhone
) {}
