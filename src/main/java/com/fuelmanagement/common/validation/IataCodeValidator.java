package com.fuelmanagement.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IataCodeValidator implements ConstraintValidator<IataCode, String> {

    private static final Pattern IATA_PATTERN = Pattern.compile("[A-Z]{3}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // IATA is optional — null is valid
        return IATA_PATTERN.matcher(value).matches();
    }
}
