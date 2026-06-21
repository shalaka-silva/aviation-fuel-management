package com.fuelmanagement.vendor.application;

import com.fuelmanagement.vendor.domain.DuplicateVendorException;
import com.fuelmanagement.vendor.domain.Vendor;
import com.fuelmanagement.vendor.domain.VendorNotFoundException;
import com.fuelmanagement.vendor.domain.VendorType;
import com.fuelmanagement.vendor.infrastructure.VendorRepository;
import com.fuelmanagement.vendor.web.CreateVendorRequest;
import com.fuelmanagement.vendor.web.VendorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorService vendorService;

    private CreateVendorRequest validRequest() {
        return new CreateVendorRequest(
                "World Fuel Services", VendorType.BROKER, "Air Total",
                "135924", "Atul Somarajan", "ASomarajan@wfscorp.com",
                "+971 561-717-942", "+44 207-808-6999", "Fuel24@wfscorp.com");
    }

    private Vendor vendorEntity(Long id, String name) {
        Vendor v = new Vendor();
        v.setName(name);
        v.setVendorType(VendorType.BROKER);
        v.setMainSupplierName("Air Total");
        v.setCustomerNumber("135924");
        v.setSalesRepName("Atul Somarajan");
        v.setSalesRepEmail("ASomarajan@wfscorp.com");
        v.setSalesRepPhone("+971 561-717-942");
        v.setLogisticsPhone("+44 207-808-6999");
        v.setLogisticsEmail("Fuel24@wfscorp.com");
        v.setActive(true);
        return v;
    }

    @Test
    void createVendor_success_returnsResponse() {
        when(vendorRepository.existsByName("World Fuel Services")).thenReturn(false);
        when(vendorRepository.save(any())).thenReturn(vendorEntity(1L, "World Fuel Services"));

        VendorResponse response = vendorService.createVendor(validRequest());

        assertThat(response.name()).isEqualTo("World Fuel Services");
        assertThat(response.vendorType()).isEqualTo(VendorType.BROKER);
        assertThat(response.mainSupplierName()).isEqualTo("Air Total");
        assertThat(response.customerNumber()).isEqualTo("135924");
        assertThat(response.active()).isTrue();
        verify(vendorRepository).save(any());
    }

    @Test
    void createVendor_duplicateName_throwsDuplicateException() {
        when(vendorRepository.existsByName("World Fuel Services")).thenReturn(true);

        assertThatThrownBy(() -> vendorService.createVendor(validRequest()))
                .isInstanceOf(DuplicateVendorException.class)
                .hasMessageContaining("World Fuel Services");

        verify(vendorRepository, never()).save(any());
    }

    @Test
    void getById_found_returnsResponse() {
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendorEntity(1L, "World Fuel Services")));

        VendorResponse response = vendorService.getById(1L);

        assertThat(response.name()).isEqualTo("World Fuel Services");
    }

    @Test
    void getById_notFound_throwsNotFoundException() {
        when(vendorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vendorService.getById(99L))
                .isInstanceOf(VendorNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void listVendors_activeOnly_usesActiveTrueQuery() {
        when(vendorRepository.findAllByActiveTrue(any()))
                .thenReturn(new PageImpl<>(List.of(vendorEntity(1L, "World Fuel Services"))));

        var page = vendorService.listVendors(PageRequest.of(0, 10), false);

        assertThat(page.getContent()).hasSize(1);
        verify(vendorRepository).findAllByActiveTrue(any());
        verify(vendorRepository, never()).findAll(any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void listVendors_includeInactive_usesFullQuery() {
        when(vendorRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(vendorEntity(1L, "World Fuel Services"))));

        var page = vendorService.listVendors(PageRequest.of(0, 10), true);

        assertThat(page.getContent()).hasSize(1);
        verify(vendorRepository).findAll(any(org.springframework.data.domain.Pageable.class));
        verify(vendorRepository, never()).findAllByActiveTrue(any());
    }

    @Test
    void deactivateVendor_found_setsActiveFalse() {
        Vendor vendor = vendorEntity(1L, "World Fuel Services");
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));
        when(vendorRepository.save(vendor)).thenReturn(vendor);

        VendorResponse response = vendorService.deactivateVendor(1L);

        assertThat(vendor.isActive()).isFalse();
        assertThat(response.active()).isFalse();
        verify(vendorRepository).save(vendor);
    }

    @Test
    void deactivateVendor_notFound_throwsNotFoundException() {
        when(vendorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vendorService.deactivateVendor(99L))
                .isInstanceOf(VendorNotFoundException.class);
    }
}
