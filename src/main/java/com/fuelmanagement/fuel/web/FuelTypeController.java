package com.fuelmanagement.fuel.web;

import com.fuelmanagement.fuel.application.FuelTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fuel-types")
public class FuelTypeController {

    private final FuelTypeService fuelTypeService;

    public FuelTypeController(FuelTypeService fuelTypeService) {
        this.fuelTypeService = fuelTypeService;
    }

    @PostMapping
    public ResponseEntity<FuelTypeResponse> createFuelType(@Valid @RequestBody CreateFuelTypeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fuelTypeService.createFuelType(request));
    }

    @GetMapping("/{code}")
    public FuelTypeResponse getByCode(@PathVariable String code) {
        return fuelTypeService.getByCode(code);
    }

    @GetMapping
    public List<FuelTypeResponse> listActiveFuelTypes() {
        return fuelTypeService.listActiveFuelTypes();
    }

    @PatchMapping("/{code}/deactivate")
    public FuelTypeResponse deactivateFuelType(@PathVariable String code) {
        return fuelTypeService.deactivateFuelType(code);
    }
}
