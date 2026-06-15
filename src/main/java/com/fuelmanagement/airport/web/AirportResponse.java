package com.fuelmanagement.airport.web;

import com.fuelmanagement.airport.domain.Airport;
import java.math.BigDecimal;

public record AirportResponse(
        Long id,
        String icaoCode,
        String iataCode,
        String name,
        String city,
        String country,
        BigDecimal latitude,
        BigDecimal longitude,
        String timezone,
        Integer elevation,
        boolean active
) {
    public static AirportResponse from(Airport airport) {
        return new AirportResponse(
                airport.getId(),
                airport.getIcaoCode(),
                airport.getIataCode(),
                airport.getName(),
                airport.getCity(),
                airport.getCountry(),
                airport.getLatitude(),
                airport.getLongitude(),
                airport.getTimezone(),
                airport.getElevation(),
                airport.isActive()
        );
    }
}
