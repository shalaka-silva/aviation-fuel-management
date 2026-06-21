package com.fuelmanagement.vendor.domain;

public class DuplicateVendorException extends RuntimeException {
    public DuplicateVendorException(String message) {
        super(message);
    }
}
