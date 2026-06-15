package com.fuelmanagement.airport.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuelmanagement.airport.application.AirportImportResult;
import com.fuelmanagement.airport.application.AirportImportRow;
import com.fuelmanagement.airport.application.AirportImportService;
import com.fuelmanagement.airport.application.AirportService;
import com.fuelmanagement.airport.domain.AirportNotFoundException;
import com.fuelmanagement.airport.domain.DuplicateAirportException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirportController.class)
class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AirportService airportService;

    @MockitoBean
    private AirportImportService airportImportService;

    private AirportResponse omdbResponse() {
        return new AirportResponse(1L, "OMDB", "DXB", "Dubai Intl", "Dubai", "AE",
                BigDecimal.valueOf(25.2528), BigDecimal.valueOf(55.3644), "Asia/Dubai", 19, true);
    }

    private CreateAirportRequest validCreateRequest() {
        return new CreateAirportRequest("OMDB", "DXB", "Dubai Intl", "Dubai", "AE",
                BigDecimal.valueOf(25.2528), BigDecimal.valueOf(55.3644), "Asia/Dubai", 19);
    }

    @Test
    void createAirport_validRequest_returns201() throws Exception {
        when(airportService.createAirport(any())).thenReturn(omdbResponse());

        mockMvc.perform(post("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.icaoCode").value("OMDB"))
                .andExpect(jsonPath("$.iataCode").value("DXB"));
    }

    @Test
    void createAirport_invalidIcao_returns400WithViolations() throws Exception {
        CreateAirportRequest badRequest = new CreateAirportRequest("omdb", null,
                "Airport", "City", "AE", BigDecimal.ONE, BigDecimal.ONE, "UTC", null);

        mockMvc.perform(post("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations").exists())
                .andExpect(jsonPath("$.violations.icaoCode").exists());
    }

    @Test
    void createAirport_duplicate_returns409() throws Exception {
        when(airportService.createAirport(any()))
                .thenThrow(new DuplicateAirportException("Airport already exists: OMDB"));

        mockMvc.perform(post("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateRequest())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void getByIcaoCode_found_returns200() throws Exception {
        when(airportService.getByIcaoCode("OMDB")).thenReturn(omdbResponse());

        mockMvc.perform(get("/api/v1/airports/OMDB"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.icaoCode").value("OMDB"));
    }

    @Test
    void getByIcaoCode_notFound_returns404() throws Exception {
        when(airportService.getByIcaoCode("ZZZZ"))
                .thenThrow(new AirportNotFoundException("Airport not found: ZZZZ"));

        mockMvc.perform(get("/api/v1/airports/ZZZZ"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Airport not found: ZZZZ"));
    }

    @Test
    void listAirports_returns200WithPage() throws Exception {
        when(airportService.listAirports(any(), any(Boolean.class)))
                .thenReturn(new PageImpl<>(List.of(omdbResponse()), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/airports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].icaoCode").value("OMDB"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void deactivateAirport_found_returns200() throws Exception {
        AirportResponse deactivated = new AirportResponse(1L, "OMDB", "DXB", "Dubai Intl",
                "Dubai", "AE", BigDecimal.valueOf(25.2528), BigDecimal.valueOf(55.3644),
                "Asia/Dubai", 19, false);
        when(airportService.deactivateAirport("OMDB")).thenReturn(deactivated);

        mockMvc.perform(patch("/api/v1/airports/OMDB/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void deactivateAirport_notFound_returns404() throws Exception {
        when(airportService.deactivateAirport("ZZZZ"))
                .thenThrow(new AirportNotFoundException("Airport not found: ZZZZ"));

        mockMvc.perform(patch("/api/v1/airports/ZZZZ/deactivate"))
                .andExpect(status().isNotFound());
    }

    @Test
    void importAirports_validBatch_returns200WithResult() throws Exception {
        AirportImportResult result = new AirportImportResult(1, 1, 0, List.of());
        when(airportImportService.importAirports(anyString(), any())).thenReturn(result);

        AirportImportRow row = new AirportImportRow("OMDB", "DXB", "Dubai Intl", "Dubai", "AE",
                BigDecimal.valueOf(25.2528), BigDecimal.valueOf(55.3644), "Asia/Dubai", null, "raw");
        ImportAirportsRequest request = new ImportAirportsRequest("batch-1", List.of(row));

        mockMvc.perform(post("/api/v1/airports/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRows").value(1))
                .andExpect(jsonPath("$.successCount").value(1));
    }

    @Test
    void importAirports_missingBatchId_returns400() throws Exception {
        ImportAirportsRequest request = new ImportAirportsRequest("", List.of());

        mockMvc.perform(post("/api/v1/airports/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
