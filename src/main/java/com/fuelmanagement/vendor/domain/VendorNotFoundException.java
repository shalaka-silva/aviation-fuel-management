package com.fuelmanagement.vendor.domain;

public class VendorNotFoundException extends RuntimeException {
    public VendorNotFoundException(String message) {
        super(message);
    }
}
