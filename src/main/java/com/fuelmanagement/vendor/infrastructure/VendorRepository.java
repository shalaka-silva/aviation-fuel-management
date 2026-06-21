package com.fuelmanagement.vendor.infrastructure;

import com.fuelmanagement.vendor.domain.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    boolean existsByName(String name);
    Optional<Vendor> findByName(String name);
    Page<Vendor> findAllByActiveTrue(Pageable pageable);
}
