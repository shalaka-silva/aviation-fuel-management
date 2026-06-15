package com.fuelmanagement.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IataCodeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IataCode {
    String message() default "must be a valid IATA code (3 uppercase letters, e.g. DXB)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
