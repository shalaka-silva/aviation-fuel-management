package com.fuelmanagement.common.error;

import com.fuelmanagement.airport.domain.AirportNotFoundException;
import com.fuelmanagement.airport.domain.DuplicateAirportException;
import com.fuelmanagement.fuel.domain.DuplicateFuelTypeException;
import com.fuelmanagement.fuel.domain.FuelTypeNotFoundException;
import com.fuelmanagement.vendor.domain.DuplicateIntoPlaneAgentException;
import com.fuelmanagement.vendor.domain.DuplicateVendorException;
import com.fuelmanagement.vendor.domain.IntoPlaneAgentNotFoundException;
import com.fuelmanagement.vendor.domain.VendorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AirportNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAirportNotFound(AirportNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorBody(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateAirportException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateAirport(DuplicateAirportException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorBody(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(VendorNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleVendorNotFound(VendorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorBody(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateVendorException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateVendor(DuplicateVendorException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorBody(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(IntoPlaneAgentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAgentNotFound(IntoPlaneAgentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorBody(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateIntoPlaneAgentException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateAgent(DuplicateIntoPlaneAgentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorBody(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(FuelTypeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleFuelTypeNotFound(FuelTypeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorBody(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateFuelTypeException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateFuelType(DuplicateFuelTypeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorBody(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, List<String>> violations = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));
        Map<String, Object> body = new LinkedHashMap<>(errorBody(400, "Bad Request", "Validation failed"));
        body.put("violations", violations);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorBody(500, "Internal Server Error", ex.getMessage()));
    }

    private Map<String, Object> errorBody(int status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status);
        body.put("error", error);
        body.put("message", message);
        body.put("timestamp", Instant.now().toString());
        return body;
    }
}
