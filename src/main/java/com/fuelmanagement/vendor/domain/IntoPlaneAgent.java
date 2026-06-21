package com.fuelmanagement.vendor.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "into_plane_agents",
    uniqueConstraints = @UniqueConstraint(name = "uq_into_plane_agents_code", columnNames = "code")
)
public class IntoPlaneAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "operating_hours", length = 100)
    private String operatingHours;

    @Column(name = "minimum_notice_required", length = 100)
    private String minimumNoticeRequired;

    @Column(name = "ramps_serviced", length = 300)
    private String rampsServiced;

    @Column(name = "contact_email", length = 200)
    private String contactEmail;

    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public IntoPlaneAgent() {}

    public Long getId() { return id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOperatingHours() { return operatingHours; }
    public void setOperatingHours(String operatingHours) { this.operatingHours = operatingHours; }

    public String getMinimumNoticeRequired() { return minimumNoticeRequired; }
    public void setMinimumNoticeRequired(String minimumNoticeRequired) { this.minimumNoticeRequired = minimumNoticeRequired; }

    public String getRampsServiced() { return rampsServiced; }
    public void setRampsServiced(String rampsServiced) { this.rampsServiced = rampsServiced; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
}
