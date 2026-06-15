package com.fuelmanagement.airport.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(
    name = "airports",
    uniqueConstraints = @UniqueConstraint(name = "uq_airports_icao_code", columnNames = "icao_code")
)
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "icao_code", nullable = false, length = 4)
    private String icaoCode;

    @Column(name = "iata_code", length = 3)
    private String iataCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String country;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 19, scale = 6)
    private BigDecimal longitude;

    @Column(nullable = false, length = 100)
    private String timezone;

    @Column
    private Integer elevation;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public Airport() {}

    public Long getId() { return id; }

    public String getIcaoCode() { return icaoCode; }
    public void setIcaoCode(String icaoCode) { this.icaoCode = icaoCode; }

    public String getIataCode() { return iataCode; }
    public void setIataCode(String iataCode) { this.iataCode = iataCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public Integer getElevation() { return elevation; }
    public void setElevation(Integer elevation) { this.elevation = elevation; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
