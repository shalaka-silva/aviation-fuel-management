package com.fuelmanagement.vendor.infrastructure;

import com.fuelmanagement.vendor.domain.VendorContractTerms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorContractTermsRepository extends JpaRepository<VendorContractTerms, Long> {
    Optional<VendorContractTerms> findByVendorId(Long vendorId);
    boolean existsByVendorId(Long vendorId);
}
