package com.fuelmanagement.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IcaoCodeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IcaoCode {
    String message() default "must be a valid ICAO code (4 uppercase letters, e.g. OMDB)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
