package com.fuelmanagement.airport.application;

import java.util.List;

public record AirportImportResult(
        int totalRows,
        int successCount,
        int failureCount,
        List<AirportImportRowError> errors
) {
    public record AirportImportRowError(
            int rowIndex,
            String icaoCode,
            List<String> messages
    ) {}
}
