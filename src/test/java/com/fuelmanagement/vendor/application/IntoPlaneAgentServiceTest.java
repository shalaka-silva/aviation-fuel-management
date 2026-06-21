package com.fuelmanagement.vendor.application;

import com.fuelmanagement.vendor.domain.DuplicateIntoPlaneAgentException;
import com.fuelmanagement.vendor.domain.IntoPlaneAgent;
import com.fuelmanagement.vendor.domain.IntoPlaneAgentNotFoundException;
import com.fuelmanagement.vendor.infrastructure.IntoPlaneAgentRepository;
import com.fuelmanagement.vendor.web.CreateIntoPlaneAgentRequest;
import com.fuelmanagement.vendor.web.IntoPlaneAgentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntoPlaneAgentServiceTest {

    @Mock
    private IntoPlaneAgentRepository agentRepository;

    @InjectMocks
    private IntoPlaneAgentService agentService;

    private CreateIntoPlaneAgentRequest validRequest() {
        return new CreateIntoPlaneAgentRequest(
                "EAK", "E.a.k.", "24 Hours operation",
                "NONE", "All ramps", null, null);
    }

    private IntoPlaneAgent agentEntity(String code) {
        IntoPlaneAgent a = new IntoPlaneAgent();
        a.setCode(code);
        a.setName("E.a.k.");
        a.setOperatingHours("24 Hours operation");
        a.setMinimumNoticeRequired("NONE");
        a.setRampsServiced("All ramps");
        a.setActive(true);
        return a;
    }

    @Test
    void createAgent_success_returnsResponse() {
        when(agentRepository.existsByCode("EAK")).thenReturn(false);
        when(agentRepository.save(any())).thenReturn(agentEntity("EAK"));

        IntoPlaneAgentResponse response = agentService.createAgent(validRequest());

        assertThat(response.code()).isEqualTo("EAK");
        assertThat(response.name()).isEqualTo("E.a.k.");
        assertThat(response.active()).isTrue();
        verify(agentRepository).save(any());
    }

    @Test
    void createAgent_duplicateCode_throwsDuplicateException() {
        when(agentRepository.existsByCode("EAK")).thenReturn(true);

        assertThatThrownBy(() -> agentService.createAgent(validRequest()))
                .isInstanceOf(DuplicateIntoPlaneAgentException.class)
                .hasMessageContaining("EAK");

        verify(agentRepository, never()).save(any());
    }

    @Test
    void getByCode_found_returnsResponse() {
        when(agentRepository.findByCode("EAK")).thenReturn(Optional.of(agentEntity("EAK")));

        IntoPlaneAgentResponse response = agentService.getByCode("EAK");

        assertThat(response.code()).isEqualTo("EAK");
        assertThat(response.operatingHours()).isEqualTo("24 Hours operation");
    }

    @Test
    void getByCode_notFound_throwsNotFoundException() {
        when(agentRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> agentService.getByCode("UNKNOWN"))
                .isInstanceOf(IntoPlaneAgentNotFoundException.class)
                .hasMessageContaining("UNKNOWN");
    }

    @Test
    void deactivateAgent_found_setsActiveFalse() {
        IntoPlaneAgent agent = agentEntity("EAK");
        when(agentRepository.findByCode("EAK")).thenReturn(Optional.of(agent));
        when(agentRepository.save(agent)).thenReturn(agent);

        IntoPlaneAgentResponse response = agentService.deactivateAgent("EAK");

        assertThat(agent.isActive()).isFalse();
        assertThat(response.active()).isFalse();
    }

    @Test
    void deactivateAgent_notFound_throwsNotFoundException() {
        when(agentRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> agentService.deactivateAgent("UNKNOWN"))
                .isInstanceOf(IntoPlaneAgentNotFoundException.class);
    }
}
