package com.fuelmanagement.vendor.web;

import com.fuelmanagement.vendor.application.VendorAirportCoverageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorAirportCoverageController {

    private final VendorAirportCoverageService coverageService;

    public VendorAirportCoverageController(VendorAirportCoverageService coverageService) {
        this.coverageService = coverageService;
    }

    @PostMapping("/{vendorId}/coverages")
    public ResponseEntity<VendorAirportCoverageResponse> registerCoverage(
            @PathVariable Long vendorId,
            @Valid @RequestBody RegisterVendorAirportCoverageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(coverageService.registerCoverage(vendorId, request));
    }

    @GetMapping("/{vendorId}/coverages")
    public List<VendorAirportCoverageResponse> listByVendor(@PathVariable Long vendorId) {
        return coverageService.listByVendor(vendorId);
    }

    @PatchMapping("/{vendorId}/coverages/{coverageId}/deactivate")
    public VendorAirportCoverageResponse deactivateCoverage(
            @PathVariable Long vendorId,
            @PathVariable Long coverageId) {
        return coverageService.deactivateCoverage(coverageId);
    }
}
