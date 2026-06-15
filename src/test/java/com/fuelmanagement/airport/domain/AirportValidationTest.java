package com.fuelmanagement.airport.domain;

import com.fuelmanagement.airport.web.CreateAirportRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AirportValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private CreateAirportRequest request(String icaoCode, String iataCode) {
        return new CreateAirportRequest(icaoCode, iataCode, "Test Airport", "Dubai",
                "AE", BigDecimal.valueOf(25.2528), BigDecimal.valueOf(55.3644), "Asia/Dubai", null);
    }

    @Test
    void validIcaoCode_passes() {
        assertThat(validator.validate(request("OMDB", null))).isEmpty();
        assertThat(validator.validate(request("EGLL", null))).isEmpty();
        assertThat(validator.validate(request("KJFK", null))).isEmpty();
    }

    @Test
    void icaoCodeTooShort_fails() {
        assertThat(violations(request("OMD", null), "icaoCode")).isNotEmpty();
    }

    @Test
    void icaoCodeTooLong_fails() {
        assertThat(violations(request("OMDBA", null), "icaoCode")).isNotEmpty();
    }

    @Test
    void icaoCodeLowercase_fails() {
        assertThat(violations(request("omdb", null), "icaoCode")).isNotEmpty();
    }

    @Test
    void icaoCodeWithDigit_fails() {
        assertThat(violations(request("OMD1", null), "icaoCode")).isNotEmpty();
    }

    @Test
    void icaoCodeNull_fails() {
        assertThat(violations(request(null, null), "icaoCode")).isNotEmpty();
    }

    @Test
    void nullIataCode_passes() {
        assertThat(validator.validate(request("OMDB", null))).isEmpty();
    }

    @Test
    void validIataCode_passes() {
        assertThat(validator.validate(request("OMDB", "DXB"))).isEmpty();
    }

    @Test
    void iataCodeTooShort_fails() {
        assertThat(violations(request("OMDB", "DX"), "iataCode")).isNotEmpty();
    }

    @Test
    void iataCodeTooLong_fails() {
        assertThat(violations(request("OMDB", "DXBA"), "iataCode")).isNotEmpty();
    }

    @Test
    void iataCodeLowercase_fails() {
        assertThat(violations(request("OMDB", "dxb"), "iataCode")).isNotEmpty();
    }

    private Set<ConstraintViolation<CreateAirportRequest>> violations(CreateAirportRequest req, String field) {
        return validator.validate(req).stream()
                .filter(v -> v.getPropertyPath().toString().equals(field))
                .collect(java.util.stream.Collectors.toSet());
    }
}
