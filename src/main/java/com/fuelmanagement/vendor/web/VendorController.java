package com.fuelmanagement.vendor.web;

import com.fuelmanagement.vendor.application.VendorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public ResponseEntity<VendorResponse> createVendor(@Valid @RequestBody CreateVendorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorService.createVendor(request));
    }

    @GetMapping("/{id}")
    public VendorResponse getById(@PathVariable Long id) {
        return vendorService.getById(id);
    }

    @GetMapping
    public Page<VendorResponse> listVendors(
            Pageable pageable,
            @RequestParam(defaultValue = "false") boolean includeInactive) {
        return vendorService.listVendors(pageable, includeInactive);
    }

    @PatchMapping("/{id}/deactivate")
    public VendorResponse deactivateVendor(@PathVariable Long id) {
        return vendorService.deactivateVendor(id);
    }
}
