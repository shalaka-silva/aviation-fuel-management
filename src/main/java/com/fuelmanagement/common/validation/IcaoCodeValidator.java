package com.fuelmanagement.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IcaoCodeValidator implements ConstraintValidator<IcaoCode, String> {

    private static final Pattern ICAO_PATTERN = Pattern.compile("[A-Z]{4}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // null handled separately by @NotNull
        return ICAO_PATTERN.matcher(value).matches();
    }
}
