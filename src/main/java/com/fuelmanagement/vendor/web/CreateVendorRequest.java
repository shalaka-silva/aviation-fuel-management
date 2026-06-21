package com.fuelmanagement.vendor.web;

import com.fuelmanagement.vendor.domain.VendorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateVendorRequest(
        @NotBlank @Size(max = 200) String name,
        @NotNull VendorType vendorType,
        @Size(max = 200) String mainSupplierName,
        @Size(max = 50) String customerNumber,
        @Size(max = 200) String salesRepName,
        @Size(max = 200) String salesRepEmail,
        @Size(max = 50) String salesRepPhone,
        @Size(max = 50) String logisticsPhone,
        @Size(max = 200) String logisticsEmail
) {}
