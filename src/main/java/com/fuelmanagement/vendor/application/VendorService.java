package com.fuelmanagement.vendor.application;

import com.fuelmanagement.vendor.domain.Vendor;
import com.fuelmanagement.vendor.domain.DuplicateVendorException;
import com.fuelmanagement.vendor.domain.VendorNotFoundException;
import com.fuelmanagement.vendor.infrastructure.VendorRepository;
import com.fuelmanagement.vendor.web.CreateVendorRequest;
import com.fuelmanagement.vendor.web.VendorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public VendorResponse createVendor(CreateVendorRequest request) {
        if (vendorRepository.existsByName(request.name())) {
            throw new DuplicateVendorException("Vendor already exists with name: " + request.name());
        }
        Vendor vendor = new Vendor();
        vendor.setName(request.name());
        vendor.setVendorType(request.vendorType());
        vendor.setMainSupplierName(request.mainSupplierName());
        vendor.setCustomerNumber(request.customerNumber());
        vendor.setSalesRepName(request.salesRepName());
        vendor.setSalesRepEmail(request.salesRepEmail());
        vendor.setSalesRepPhone(request.salesRepPhone());
        vendor.setLogisticsPhone(request.logisticsPhone());
        vendor.setLogisticsEmail(request.logisticsEmail());
        vendor.setActive(true);
        return VendorResponse.from(vendorRepository.save(vendor));
    }

    @Transactional(readOnly = true)
    public VendorResponse getById(Long id) {
        return vendorRepository.findById(id)
                .map(VendorResponse::from)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<VendorResponse> listVendors(Pageable pageable, boolean includeInactive) {
        if (includeInactive) {
            return vendorRepository.findAll(pageable).map(VendorResponse::from);
        }
        return vendorRepository.findAllByActiveTrue(pageable).map(VendorResponse::from);
    }

    public VendorResponse deactivateVendor(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + id));
        vendor.setActive(false);
        return VendorResponse.from(vendorRepository.save(vendor));
    }
}
