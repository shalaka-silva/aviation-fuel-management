package com.fuelmanagement.vendor.application;

import com.fuelmanagement.airport.domain.Airport;
import com.fuelmanagement.airport.domain.AirportNotFoundException;
import com.fuelmanagement.airport.infrastructure.AirportRepository;
import com.fuelmanagement.fuel.domain.FuelType;
import com.fuelmanagement.fuel.domain.FuelTypeNotFoundException;
import com.fuelmanagement.fuel.infrastructure.FuelTypeRepository;
import com.fuelmanagement.vendor.domain.*;
import com.fuelmanagement.vendor.infrastructure.IntoPlaneAgentRepository;
import com.fuelmanagement.vendor.infrastructure.VendorAirportCoverageRepository;
import com.fuelmanagement.vendor.infrastructure.VendorRepository;
import com.fuelmanagement.vendor.web.RegisterVendorAirportCoverageRequest;
import com.fuelmanagement.vendor.web.VendorAirportCoverageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VendorAirportCoverageService {

    private final VendorAirportCoverageRepository coverageRepository;
    private final VendorRepository vendorRepository;
    private final AirportRepository airportRepository;
    private final FuelTypeRepository fuelTypeRepository;
    private final IntoPlaneAgentRepository agentRepository;

    public VendorAirportCoverageService(
            VendorAirportCoverageRepository coverageRepository,
            VendorRepository vendorRepository,
            AirportRepository airportRepository,
            FuelTypeRepository fuelTypeRepository,
            IntoPlaneAgentRepository agentRepository) {
        this.coverageRepository = coverageRepository;
        this.vendorRepository = vendorRepository;
        this.airportRepository = airportRepository;
        this.fuelTypeRepository = fuelTypeRepository;
        this.agentRepository = agentRepository;
    }

    public VendorAirportCoverageResponse registerCoverage(Long vendorId,
                                                           RegisterVendorAirportCoverageRequest request) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException("Vendor not found with id: " + vendorId));

        Airport airport = airportRepository.findByIcaoCode(request.icaoCode())
                .orElseThrow(() -> new AirportNotFoundException("Airport not found: " + request.icaoCode()));

        FuelType fuelType = fuelTypeRepository.findByCode(request.fuelTypeCode().toUpperCase())
                .orElseThrow(() -> new FuelTypeNotFoundException(
                        "Fuel type not found with code: " + request.fuelTypeCode()));

        IntoPlaneAgent agent = null;
        if (request.intoPlaneAgentCode() != null) {
            agent = agentRepository.findByCode(request.intoPlaneAgentCode().toUpperCase())
                    .orElseThrow(() -> new IntoPlaneAgentNotFoundException(
                            "Into-plane agent not found with code: " + request.intoPlaneAgentCode()));
        }

        Long agentId = agent != null ? agent.getId() : null;
        if (coverageRepository.existsDuplicate(vendorId, airport.getId(), fuelType.getId(), agentId)) {
            throw new DuplicateVendorException(
                    "Coverage already registered for this vendor/airport/fuel-type/agent combination");
        }

        VendorAirportCoverage coverage = new VendorAirportCoverage();
        coverage.setVendor(vendor);
        coverage.setAirport(airport);
        coverage.setFuelType(fuelType);
        coverage.setIntoPlaneAgent(agent);
        coverage.setNotes(request.notes());
        coverage.setActive(true);

        return VendorAirportCoverageResponse.from(coverageRepository.save(coverage));
    }

    @Transactional(readOnly = true)
    public List<VendorAirportCoverageResponse> listByVendor(Long vendorId) {
        if (!vendorRepository.existsById(vendorId)) {
            throw new VendorNotFoundException("Vendor not found with id: " + vendorId);
        }
        return coverageRepository
                .findByVendorIdAndActiveTrueOrderByAirportIcaoCode(vendorId)
                .stream()
                .map(VendorAirportCoverageResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VendorAirportCoverageResponse> listByAirport(String icaoCode) {
        Airport airport = airportRepository.findByIcaoCode(icaoCode)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found: " + icaoCode));
        return coverageRepository
                .findByAirportIdAndActiveTrueOrderByVendorName(airport.getId())
                .stream()
                .map(VendorAirportCoverageResponse::from)
                .toList();
    }

    public VendorAirportCoverageResponse deactivateCoverage(Long coverageId) {
        VendorAirportCoverage coverage = coverageRepository.findById(coverageId)
                .orElseThrow(() -> new VendorNotFoundException(
                        "Coverage not found with id: " + coverageId));
        coverage.setActive(false);
        return VendorAirportCoverageResponse.from(coverageRepository.save(coverage));
    }
}
