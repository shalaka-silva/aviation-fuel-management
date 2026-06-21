package com.fuelmanagement.vendor.application;

import com.fuelmanagement.vendor.domain.DuplicateIntoPlaneAgentException;
import com.fuelmanagement.vendor.domain.IntoPlaneAgent;
import com.fuelmanagement.vendor.domain.IntoPlaneAgentNotFoundException;
import com.fuelmanagement.vendor.infrastructure.IntoPlaneAgentRepository;
import com.fuelmanagement.vendor.web.CreateIntoPlaneAgentRequest;
import com.fuelmanagement.vendor.web.IntoPlaneAgentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IntoPlaneAgentService {

    private final IntoPlaneAgentRepository agentRepository;

    public IntoPlaneAgentService(IntoPlaneAgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public IntoPlaneAgentResponse createAgent(CreateIntoPlaneAgentRequest request) {
        if (agentRepository.existsByCode(request.code())) {
            throw new DuplicateIntoPlaneAgentException(
                    "Into-plane agent already exists with code: " + request.code());
        }
        IntoPlaneAgent agent = new IntoPlaneAgent();
        agent.setCode(request.code().toUpperCase());
        agent.setName(request.name());
        agent.setOperatingHours(request.operatingHours());
        agent.setMinimumNoticeRequired(request.minimumNoticeRequired());
        agent.setRampsServiced(request.rampsServiced());
        agent.setContactEmail(request.contactEmail());
        agent.setContactPhone(request.contactPhone());
        agent.setActive(true);
        return IntoPlaneAgentResponse.from(agentRepository.save(agent));
    }

    @Transactional(readOnly = true)
    public IntoPlaneAgentResponse getByCode(String code) {
        return agentRepository.findByCode(code.toUpperCase())
                .map(IntoPlaneAgentResponse::from)
                .orElseThrow(() -> new IntoPlaneAgentNotFoundException(
                        "Into-plane agent not found with code: " + code));
    }

    @Transactional(readOnly = true)
    public Page<IntoPlaneAgentResponse> listAgents(Pageable pageable, boolean includeInactive) {
        if (includeInactive) {
            return agentRepository.findAll(pageable).map(IntoPlaneAgentResponse::from);
        }
        return agentRepository.findAllByActiveTrue(pageable).map(IntoPlaneAgentResponse::from);
    }

    public IntoPlaneAgentResponse deactivateAgent(String code) {
        IntoPlaneAgent agent = agentRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new IntoPlaneAgentNotFoundException(
                        "Into-plane agent not found with code: " + code));
        agent.setActive(false);
        return IntoPlaneAgentResponse.from(agentRepository.save(agent));
    }
}
