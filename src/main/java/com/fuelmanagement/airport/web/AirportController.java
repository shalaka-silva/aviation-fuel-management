package com.fuelmanagement.airport.web;

import com.fuelmanagement.airport.application.AirportImportResult;
import com.fuelmanagement.airport.application.AirportImportService;
import com.fuelmanagement.airport.application.AirportService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/airports")
public class AirportController {

    private final AirportService airportService;
    private final AirportImportService airportImportService;

    public AirportController(AirportService airportService, AirportImportService airportImportService) {
        this.airportService = airportService;
        this.airportImportService = airportImportService;
    }

    @PostMapping
    public ResponseEntity<AirportResponse> createAirport(@Valid @RequestBody CreateAirportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(airportService.createAirport(request));
    }

    @GetMapping("/{icaoCode}")
    public AirportResponse getByIcaoCode(@PathVariable String icaoCode) {
        return airportService.getByIcaoCode(icaoCode);
    }

    @GetMapping
    public Page<AirportResponse> listAirports(
            Pageable pageable,
            @RequestParam(defaultValue = "false") boolean includeInactive) {
        return airportService.listAirports(pageable, includeInactive);
    }

    @PatchMapping("/{icaoCode}/deactivate")
    public AirportResponse deactivateAirport(@PathVariable String icaoCode) {
        return airportService.deactivateAirport(icaoCode);
    }

    @PostMapping("/import")
    public AirportImportResult importAirports(@Valid @RequestBody ImportAirportsRequest request) {
        return airportImportService.importAirports(request.batchId(), request.airports());
    }
}
