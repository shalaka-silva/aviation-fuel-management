package com.fuelmanagement.vendor.domain;

import com.fuelmanagement.airport.domain.Airport;
import com.fuelmanagement.fuel.domain.FuelType;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "vendor_airport_coverages")
public class VendorAirportCoverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_vac_vendor"))
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "airport_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_vac_airport"))
    private Airport airport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fuel_type_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_vac_fuel_type"))
    private FuelType fuelType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "into_plane_agent_id",
                foreignKey = @ForeignKey(name = "fk_vac_into_plane_agent"))
    private IntoPlaneAgent intoPlaneAgent;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public VendorAirportCoverage() {}

    public Long getId() { return id; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public Airport getAirport() { return airport; }
    public void setAirport(Airport airport) { this.airport = airport; }

    public FuelType getFuelType() { return fuelType; }
    public void setFuelType(FuelType fuelType) { this.fuelType = fuelType; }

    public IntoPlaneAgent getIntoPlaneAgent() { return intoPlaneAgent; }
    public void setIntoPlaneAgent(IntoPlaneAgent intoPlaneAgent) { this.intoPlaneAgent = intoPlaneAgent; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
}
