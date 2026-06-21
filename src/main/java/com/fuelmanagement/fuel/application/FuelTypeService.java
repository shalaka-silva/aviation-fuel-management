package com.fuelmanagement.fuel.application;

import com.fuelmanagement.fuel.domain.DuplicateFuelTypeException;
import com.fuelmanagement.fuel.domain.FuelType;
import com.fuelmanagement.fuel.domain.FuelTypeNotFoundException;
import com.fuelmanagement.fuel.infrastructure.FuelTypeRepository;
import com.fuelmanagement.fuel.web.CreateFuelTypeRequest;
import com.fuelmanagement.fuel.web.FuelTypeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FuelTypeService {

    private final FuelTypeRepository fuelTypeRepository;

    public FuelTypeService(FuelTypeRepository fuelTypeRepository) {
        this.fuelTypeRepository = fuelTypeRepository;
    }

    public FuelTypeResponse createFuelType(CreateFuelTypeRequest request) {
        if (fuelTypeRepository.existsByCode(request.code())) {
            throw new DuplicateFuelTypeException("Fuel type already exists with code: " + request.code());
        }
        FuelType fuelType = new FuelType();
        fuelType.setCode(request.code().toUpperCase());
        fuelType.setName(request.name());
        fuelType.setDefaultUnitOfMeasure(request.defaultUnitOfMeasure());
        fuelType.setActive(true);
        return FuelTypeResponse.from(fuelTypeRepository.save(fuelType));
    }

    @Transactional(readOnly = true)
    public FuelTypeResponse getByCode(String code) {
        return fuelTypeRepository.findByCode(code.toUpperCase())
                .map(FuelTypeResponse::from)
                .orElseThrow(() -> new FuelTypeNotFoundException("Fuel type not found with code: " + code));
    }

    @Transactional(readOnly = true)
    public List<FuelTypeResponse> listActiveFuelTypes() {
        return fuelTypeRepository.findAllByActiveTrue().stream()
                .map(FuelTypeResponse::from)
                .toList();
    }

    public FuelTypeResponse deactivateFuelType(String code) {
        FuelType fuelType = fuelTypeRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new FuelTypeNotFoundException("Fuel type not found with code: " + code));
        fuelType.setActive(false);
        return FuelTypeResponse.from(fuelTypeRepository.save(fuelType));
    }
}
