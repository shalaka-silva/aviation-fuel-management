package com.fuelmanagement.vendor.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
    name = "vendors",
    uniqueConstraints = @UniqueConstraint(name = "uq_vendors_name", columnNames = "name")
)
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor_type", nullable = false, length = 20)
    private VendorType vendorType;

    @Column(name = "main_supplier_name", length = 200)
    private String mainSupplierName;

    @Column(name = "customer_number", length = 50)
    private String customerNumber;

    @Column(name = "sales_rep_name", length = 200)
    private String salesRepName;

    @Column(name = "sales_rep_email", length = 200)
    private String salesRepEmail;

    @Column(name = "sales_rep_phone", length = 50)
    private String salesRepPhone;

    @Column(name = "logistics_phone", length = 50)
    private String logisticsPhone;

    @Column(name = "logistics_email", length = 200)
    private String logisticsEmail;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Vendor() {}

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public VendorType getVendorType() { return vendorType; }
    public void setVendorType(VendorType vendorType) { this.vendorType = vendorType; }

    public String getMainSupplierName() { return mainSupplierName; }
    public void setMainSupplierName(String mainSupplierName) { this.mainSupplierName = mainSupplierName; }

    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }

    public String getSalesRepName() { return salesRepName; }
    public void setSalesRepName(String salesRepName) { this.salesRepName = salesRepName; }

    public String getSalesRepEmail() { return salesRepEmail; }
    public void setSalesRepEmail(String salesRepEmail) { this.salesRepEmail = salesRepEmail; }

    public String getSalesRepPhone() { return salesRepPhone; }
    public void setSalesRepPhone(String salesRepPhone) { this.salesRepPhone = salesRepPhone; }

    public String getLogisticsPhone() { return logisticsPhone; }
    public void setLogisticsPhone(String logisticsPhone) { this.logisticsPhone = logisticsPhone; }

    public String getLogisticsEmail() { return logisticsEmail; }
    public void setLogisticsEmail(String logisticsEmail) { this.logisticsEmail = logisticsEmail; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
}
