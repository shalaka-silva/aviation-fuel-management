package com.fuelmanagement.fuel.domain;

import jakarta.persistence.*;

@Entity
@Table(
    name = "fuel_types",
    uniqueConstraints = @UniqueConstraint(name = "uq_fuel_types_code", columnNames = "code")
)
public class FuelType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_unit_of_measure", nullable = false, length = 10)
    private UnitOfMeasure defaultUnitOfMeasure;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public FuelType() {}

    public Long getId() { return id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UnitOfMeasure getDefaultUnitOfMeasure() { return defaultUnitOfMeasure; }
    public void setDefaultUnitOfMeasure(UnitOfMeasure defaultUnitOfMeasure) { this.defaultUnitOfMeasure = defaultUnitOfMeasure; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
