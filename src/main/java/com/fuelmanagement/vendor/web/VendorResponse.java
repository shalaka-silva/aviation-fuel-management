package com.fuelmanagement.vendor.web;

import com.fuelmanagement.vendor.domain.Vendor;
import com.fuelmanagement.vendor.domain.VendorType;

import java.time.Instant;

public record VendorResponse(
        Long id,
        String name,
        VendorType vendorType,
        String mainSupplierName,
        String customerNumber,
        String salesRepName,
        String salesRepEmail,
        String salesRepPhone,
        String logisticsPhone,
        String logisticsEmail,
        boolean active,
        Instant createdAt
) {
    public static VendorResponse from(Vendor vendor) {
        return new VendorResponse(
                vendor.getId(),
                vendor.getName(),
                vendor.getVendorType(),
                vendor.getMainSupplierName(),
                vendor.getCustomerNumber(),
                vendor.getSalesRepName(),
                vendor.getSalesRepEmail(),
                vendor.getSalesRepPhone(),
                vendor.getLogisticsPhone(),
                vendor.getLogisticsEmail(),
                vendor.isActive(),
                vendor.getCreatedAt()
        );
    }
}
