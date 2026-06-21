package com.fuelmanagement.vendor.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuelmanagement.vendor.application.VendorAirportCoverageService;
import com.fuelmanagement.vendor.application.VendorService;
import com.fuelmanagement.vendor.domain.DuplicateVendorException;
import com.fuelmanagement.vendor.domain.VendorNotFoundException;
import com.fuelmanagement.vendor.domain.VendorType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({VendorController.class, VendorAirportCoverageController.class})
class VendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VendorService vendorService;

    @MockitoBean
    private VendorAirportCoverageService coverageService;

    private VendorResponse wfsResponse() {
        return new VendorResponse(1L, "World Fuel Services", VendorType.BROKER, "Air Total",
                "135924", "Atul Somarajan", "atul@wfscorp.com", "+971 561-717-942",
                "+44 207-808-6999", "fuel24@wfscorp.com", true, Instant.now());
    }

    private CreateVendorRequest validCreateRequest() {
        return new CreateVendorRequest("World Fuel Services", VendorType.BROKER, "Air Total",
                "135924", "Atul Somarajan", "atul@wfscorp.com",
                "+971 561-717-942", "+44 207-808-6999", "fuel24@wfscorp.com");
    }

    @Test
    void createVendor_validRequest_returns201() throws Exception {
        when(vendorService.createVendor(any())).thenReturn(wfsResponse());

        mockMvc.perform(post("/api/v1/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("World Fuel Services"))
                .andExpect(jsonPath("$.vendorType").value("BROKER"))
                .andExpect(jsonPath("$.mainSupplierName").value("Air Total"))
                .andExpect(jsonPath("$.customerNumber").value("135924"));
    }

    @Test
    void createVendor_missingName_returns400() throws Exception {
        CreateVendorRequest badRequest = new CreateVendorRequest(
                "", VendorType.BROKER, null, null, null, null, null, null, null);

        mockMvc.perform(post("/api/v1/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists());
    }

    @Test
    void createVendor_missingVendorType_returns400() throws Exception {
        CreateVendorRequest badRequest = new CreateVendorRequest(
                "Some Vendor", null, null, null, null, null, null, null, null);

        mockMvc.perform(post("/api/v1/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createVendor_duplicate_returns409() throws Exception {
        when(vendorService.createVendor(any()))
                .thenThrow(new DuplicateVendorException("Vendor already exists: World Fuel Services"));

        mockMvc.perform(post("/api/v1/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateRequest())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void getById_found_returns200() throws Exception {
        when(vendorService.getById(1L)).thenReturn(wfsResponse());

        mockMvc.perform(get("/api/v1/vendors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("World Fuel Services"))
                .andExpect(jsonPath("$.vendorType").value("BROKER"));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        when(vendorService.getById(99L))
                .thenThrow(new VendorNotFoundException("Vendor not found with id: 99"));

        mockMvc.perform(get("/api/v1/vendors/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Vendor not found with id: 99"));
    }

    @Test
    void listVendors_returns200WithPage() throws Exception {
        when(vendorService.listVendors(any(), any(Boolean.class)))
                .thenReturn(new PageImpl<>(List.of(wfsResponse()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/vendors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("World Fuel Services"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deactivateVendor_found_returns200WithActiveFalse() throws Exception {
        VendorResponse deactivated = new VendorResponse(1L, "World Fuel Services", VendorType.BROKER,
                "Air Total", "135924", null, null, null, null, null, false, Instant.now());
        when(vendorService.deactivateVendor(1L)).thenReturn(deactivated);

        mockMvc.perform(patch("/api/v1/vendors/1/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }
}
