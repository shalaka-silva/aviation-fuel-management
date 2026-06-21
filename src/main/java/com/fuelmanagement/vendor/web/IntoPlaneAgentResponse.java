package com.fuelmanagement.vendor.web;

import com.fuelmanagement.vendor.domain.IntoPlaneAgent;

import java.time.Instant;

public record IntoPlaneAgentResponse(
        Long id,
        String code,
        String name,
        String operatingHours,
        String minimumNoticeRequired,
        String rampsServiced,
        String contactEmail,
        String contactPhone,
        boolean active,
        Instant createdAt
) {
    public static IntoPlaneAgentResponse from(IntoPlaneAgent agent) {
        return new IntoPlaneAgentResponse(
                agent.getId(),
                agent.getCode(),
                agent.getName(),
                agent.getOperatingHours(),
                agent.getMinimumNoticeRequired(),
                agent.getRampsServiced(),
                agent.getContactEmail(),
                agent.getContactPhone(),
                agent.isActive(),
                agent.getCreatedAt()
        );
    }
}
