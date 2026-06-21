package com.fuelmanagement.fuel.domain;

public class FuelTypeNotFoundException extends RuntimeException {
    public FuelTypeNotFoundException(String message) {
        super(message);
    }
}
