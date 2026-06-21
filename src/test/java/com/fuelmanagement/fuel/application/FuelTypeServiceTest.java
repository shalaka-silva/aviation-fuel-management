package com.fuelmanagement.fuel.application;

import com.fuelmanagement.fuel.domain.DuplicateFuelTypeException;
import com.fuelmanagement.fuel.domain.FuelType;
import com.fuelmanagement.fuel.domain.FuelTypeNotFoundException;
import com.fuelmanagement.fuel.domain.UnitOfMeasure;
import com.fuelmanagement.fuel.infrastructure.FuelTypeRepository;
import com.fuelmanagement.fuel.web.CreateFuelTypeRequest;
import com.fuelmanagement.fuel.web.FuelTypeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuelTypeServiceTest {

    @Mock
    private FuelTypeRepository fuelTypeRepository;

    @InjectMocks
    private FuelTypeService fuelTypeService;

    private CreateFuelTypeRequest validRequest() {
        return new CreateFuelTypeRequest("JET_A", "Jet A", UnitOfMeasure.USG);
    }

    private FuelType fuelTypeEntity(String code) {
        FuelType ft = new FuelType();
        ft.setCode(code);
        ft.setName("Jet A");
        ft.setDefaultUnitOfMeasure(UnitOfMeasure.USG);
        ft.setActive(true);
        return ft;
    }

    @Test
    void createFuelType_success_returnsResponse() {
        when(fuelTypeRepository.existsByCode("JET_A")).thenReturn(false);
        when(fuelTypeRepository.save(any())).thenReturn(fuelTypeEntity("JET_A"));

        FuelTypeResponse response = fuelTypeService.createFuelType(validRequest());

        assertThat(response.code()).isEqualTo("JET_A");
        assertThat(response.defaultUnitOfMeasure()).isEqualTo(UnitOfMeasure.USG);
        assertThat(response.active()).isTrue();
        verify(fuelTypeRepository).save(any());
    }

    @Test
    void createFuelType_duplicateCode_throwsDuplicateException() {
        when(fuelTypeRepository.existsByCode("JET_A")).thenReturn(true);

        assertThatThrownBy(() -> fuelTypeService.createFuelType(validRequest()))
                .isInstanceOf(DuplicateFuelTypeException.class)
                .hasMessageContaining("JET_A");

        verify(fuelTypeRepository, never()).save(any());
    }

    @Test
    void getByCode_found_returnsResponse() {
        when(fuelTypeRepository.findByCode("JET_A")).thenReturn(Optional.of(fuelTypeEntity("JET_A")));

        FuelTypeResponse response = fuelTypeService.getByCode("JET_A");

        assertThat(response.code()).isEqualTo("JET_A");
        assertThat(response.name()).isEqualTo("Jet A");
    }

    @Test
    void getByCode_notFound_throwsNotFoundException() {
        when(fuelTypeRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelTypeService.getByCode("UNKNOWN"))
                .isInstanceOf(FuelTypeNotFoundException.class)
                .hasMessageContaining("UNKNOWN");
    }

    @Test
    void listActiveFuelTypes_returnsOnlyActive() {
        when(fuelTypeRepository.findAllByActiveTrue())
                .thenReturn(List.of(fuelTypeEntity("JET_A"), fuelTypeEntity("JET_A1")));

        List<FuelTypeResponse> result = fuelTypeService.listActiveFuelTypes();

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(FuelTypeResponse::active);
    }

    @Test
    void deactivateFuelType_found_setsActiveFalse() {
        FuelType fuelType = fuelTypeEntity("JET_A");
        when(fuelTypeRepository.findByCode("JET_A")).thenReturn(Optional.of(fuelType));
        when(fuelTypeRepository.save(fuelType)).thenReturn(fuelType);

        FuelTypeResponse response = fuelTypeService.deactivateFuelType("JET_A");

        assertThat(fuelType.isActive()).isFalse();
        assertThat(response.active()).isFalse();
    }

    @Test
    void deactivateFuelType_notFound_throwsNotFoundException() {
        when(fuelTypeRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelTypeService.deactivateFuelType("UNKNOWN"))
                .isInstanceOf(FuelTypeNotFoundException.class);
    }
}
