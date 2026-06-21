package com.fuelmanagement.vendor.infrastructure;

import com.fuelmanagement.vendor.domain.VendorAirportCoverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VendorAirportCoverageRepository extends JpaRepository<VendorAirportCoverage, Long> {

    List<VendorAirportCoverage> findByVendorIdAndActiveTrueOrderByAirportIcaoCode(Long vendorId);

    List<VendorAirportCoverage> findByAirportIdAndActiveTrueOrderByVendorName(Long airportId);

    @Query("""
            SELECT COUNT(c) > 0 FROM VendorAirportCoverage c
            WHERE c.vendor.id = :vendorId
              AND c.airport.id = :airportId
              AND c.fuelType.id = :fuelTypeId
              AND (:agentId IS NULL AND c.intoPlaneAgent IS NULL
                   OR c.intoPlaneAgent.id = :agentId)
            """)
    boolean existsDuplicate(@Param("vendorId") Long vendorId,
                            @Param("airportId") Long airportId,
                            @Param("fuelTypeId") Long fuelTypeId,
                            @Param("agentId") Long agentId);
}
