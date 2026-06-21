package com.fuelmanagement.vendor.infrastructure;

import com.fuelmanagement.TestcontainersConfiguration;
import com.fuelmanagement.vendor.domain.Vendor;
import com.fuelmanagement.vendor.domain.VendorType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VendorRepositoryTest {

    @Autowired
    private VendorRepository vendorRepository;

    private Vendor buildVendor(String name, VendorType type, boolean active) {
        Vendor v = new Vendor();
        v.setName(name);
        v.setVendorType(type);
        v.setCustomerNumber("135924");
        v.setSalesRepName("Atul Somarajan");
        v.setSalesRepEmail("atul@wfscorp.com");
        v.setActive(active);
        return v;
    }

    @Test
    void save_and_findByName_returnsCorrectData() {
        vendorRepository.save(buildVendor("World Fuel Services", VendorType.BROKER, true));

        Optional<Vendor> found = vendorRepository.findByName("World Fuel Services");

        assertThat(found).isPresent();
        assertThat(found.get().getVendorType()).isEqualTo(VendorType.BROKER);
        assertThat(found.get().getCustomerNumber()).isEqualTo("135924");
    }

    @Test
    void existsByName_existingName_returnsTrue() {
        vendorRepository.save(buildVendor("World Fuel Services", VendorType.BROKER, true));
        assertThat(vendorRepository.existsByName("World Fuel Services")).isTrue();
    }

    @Test
    void existsByName_absentName_returnsFalse() {
        assertThat(vendorRepository.existsByName("Unknown Vendor")).isFalse();
    }

    @Test
    void duplicateName_throwsDataIntegrityViolation() {
        vendorRepository.saveAndFlush(buildVendor("World Fuel Services", VendorType.BROKER, true));

        assertThatThrownBy(() ->
                vendorRepository.saveAndFlush(buildVendor("World Fuel Services", VendorType.MAIN_SUPPLIER, true)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void findAllByActiveTrue_returnsOnlyActiveVendors() {
        vendorRepository.save(buildVendor("World Fuel Services", VendorType.BROKER, true));
        vendorRepository.save(buildVendor("Air Total", VendorType.MAIN_SUPPLIER, false));

        Page<Vendor> page = vendorRepository.findAllByActiveTrue(PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getName()).isEqualTo("World Fuel Services");
    }
}
