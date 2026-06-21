package com.fuelmanagement.vendor.web;

import com.fuelmanagement.vendor.application.IntoPlaneAgentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/into-plane-agents")
public class IntoPlaneAgentController {

    private final IntoPlaneAgentService agentService;

    public IntoPlaneAgentController(IntoPlaneAgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping
    public ResponseEntity<IntoPlaneAgentResponse> createAgent(
            @Valid @RequestBody CreateIntoPlaneAgentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agentService.createAgent(request));
    }

    @GetMapping("/{code}")
    public IntoPlaneAgentResponse getByCode(@PathVariable String code) {
        return agentService.getByCode(code);
    }

    @GetMapping
    public Page<IntoPlaneAgentResponse> listAgents(
            Pageable pageable,
            @RequestParam(defaultValue = "false") boolean includeInactive) {
        return agentService.listAgents(pageable, includeInactive);
    }

    @PatchMapping("/{code}/deactivate")
    public IntoPlaneAgentResponse deactivateAgent(@PathVariable String code) {
        return agentService.deactivateAgent(code);
    }
}
