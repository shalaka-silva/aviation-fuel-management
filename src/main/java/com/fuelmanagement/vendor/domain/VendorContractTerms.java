package com.fuelmanagement.vendor.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "vendor_contract_terms")
public class VendorContractTerms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false, unique = true,
                foreignKey = @ForeignKey(name = "fk_vendor_contract_terms_vendor"))
    private Vendor vendor;

    @Column(name = "price_index", length = 200)
    private String priceIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_frequency", length = 20)
    private PricingFrequency pricingFrequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", length = 20)
    private PaymentMode paymentMode;

    @Column(name = "credit_days")
    private Integer creditDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoicing_frequency", length = 20)
    private PricingFrequency invoicingFrequency;

    @Column(name = "offer_validity_days")
    private Integer offerValidityDays;

    @Column(name = "security_deposit_usd", precision = 19, scale = 6)
    private BigDecimal securityDepositUsd;

    @Column(name = "installment_amount_usd", precision = 19, scale = 6)
    private BigDecimal installmentAmountUsd;

    @Column(name = "days_to_send_money_prepay")
    private Integer daysToSendMoneyPrepay;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    public VendorContractTerms() {}

    @PreUpdate
    void onUpdate() { this.updatedAt = Instant.now(); }

    public Long getId() { return id; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public String getPriceIndex() { return priceIndex; }
    public void setPriceIndex(String priceIndex) { this.priceIndex = priceIndex; }

    public PricingFrequency getPricingFrequency() { return pricingFrequency; }
    public void setPricingFrequency(PricingFrequency pricingFrequency) { this.pricingFrequency = pricingFrequency; }

    public PaymentMode getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMode paymentMode) { this.paymentMode = paymentMode; }

    public Integer getCreditDays() { return creditDays; }
    public void setCreditDays(Integer creditDays) { this.creditDays = creditDays; }

    public PricingFrequency getInvoicingFrequency() { return invoicingFrequency; }
    public void setInvoicingFrequency(PricingFrequency invoicingFrequency) { this.invoicingFrequency = invoicingFrequency; }

    public Integer getOfferValidityDays() { return offerValidityDays; }
    public void setOfferValidityDays(Integer offerValidityDays) { this.offerValidityDays = offerValidityDays; }

    public BigDecimal getSecurityDepositUsd() { return securityDepositUsd; }
    public void setSecurityDepositUsd(BigDecimal securityDepositUsd) { this.securityDepositUsd = securityDepositUsd; }

    public BigDecimal getInstallmentAmountUsd() { return installmentAmountUsd; }
    public void setInstallmentAmountUsd(BigDecimal installmentAmountUsd) { this.installmentAmountUsd = installmentAmountUsd; }

    public Integer getDaysToSendMoneyPrepay() { return daysToSendMoneyPrepay; }
    public void setDaysToSendMoneyPrepay(Integer daysToSendMoneyPrepay) { this.daysToSendMoneyPrepay = daysToSendMoneyPrepay; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
