package com.fuelmanagement.fuel.domain;

public class DuplicateFuelTypeException extends RuntimeException {
    public DuplicateFuelTypeException(String message) {
        super(message);
    }
}
