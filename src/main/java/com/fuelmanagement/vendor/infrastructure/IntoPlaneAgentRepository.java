package com.fuelmanagement.vendor.infrastructure;

import com.fuelmanagement.vendor.domain.IntoPlaneAgent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IntoPlaneAgentRepository extends JpaRepository<IntoPlaneAgent, Long> {
    boolean existsByCode(String code);
    Optional<IntoPlaneAgent> findByCode(String code);
    Page<IntoPlaneAgent> findAllByActiveTrue(Pageable pageable);
}
