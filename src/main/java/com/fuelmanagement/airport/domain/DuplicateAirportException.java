package com.fuelmanagement.airport.domain;

public class DuplicateAirportException extends RuntimeException {

    public DuplicateAirportException(String message) {
        super(message);
    }
}
