package com.fuelmanagement.vendor.web;

import com.fuelmanagement.vendor.domain.VendorAirportCoverage;

import java.time.Instant;

public record VendorAirportCoverageResponse(
        Long id,
        Long vendorId,
        String vendorName,
        String icaoCode,
        String iataCode,
        String airportName,
        String fuelTypeCode,
        String fuelTypeName,
        Long intoPlaneAgentId,
        String intoPlaneAgentCode,
        String intoPlaneAgentName,
        String notes,
        boolean active,
        Instant createdAt
) {
    public static VendorAirportCoverageResponse from(VendorAirportCoverage coverage) {
        String agentCode = coverage.getIntoPlaneAgent() != null
                ? coverage.getIntoPlaneAgent().getCode() : null;
        String agentName = coverage.getIntoPlaneAgent() != null
                ? coverage.getIntoPlaneAgent().getName() : null;
        Long agentId = coverage.getIntoPlaneAgent() != null
                ? coverage.getIntoPlaneAgent().getId() : null;

        return new VendorAirportCoverageResponse(
                coverage.getId(),
                coverage.getVendor().getId(),
                coverage.getVendor().getName(),
                coverage.getAirport().getIcaoCode(),
                coverage.getAirport().getIataCode(),
                coverage.getAirport().getName(),
                coverage.getFuelType().getCode(),
                coverage.getFuelType().getName(),
                agentId,
                agentCode,
                agentName,
                coverage.getNotes(),
                coverage.isActive(),
                coverage.getCreatedAt()
        );
    }
}
