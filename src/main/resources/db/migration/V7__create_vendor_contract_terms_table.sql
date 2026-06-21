-- Vendor commercial contract terms (from supplier evaluation checklist)
-- Never edit this file after it has been applied.

CREATE TABLE vendor_contract_terms (
    id                       BIGSERIAL       PRIMARY KEY,
    vendor_id                BIGINT          NOT NULL UNIQUE REFERENCES vendors(id),
    price_index              VARCHAR(200),
    pricing_frequency        VARCHAR(20),
    payment_mode             VARCHAR(20),
    credit_days              INTEGER,
    invoicing_frequency      VARCHAR(20),
    offer_validity_days      INTEGER,
    security_deposit_usd     NUMERIC(19, 6),
    installment_amount_usd   NUMERIC(19, 6),
    days_to_send_money_prepay INTEGER,
    notes                    TEXT,
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_vendor_contract_terms_vendor FOREIGN KEY (vendor_id) REFERENCES vendors(id),
    CONSTRAINT chk_vct_payment_mode      CHECK (payment_mode       IN ('CREDIT', 'PREPAY', 'ADVANCE')),
    CONSTRAINT chk_vct_pricing_frequency CHECK (pricing_frequency  IN ('DAILY', 'WEEKLY', 'BI_WEEKLY', 'MONTHLY')),
    CONSTRAINT chk_vct_invoicing_freq    CHECK (invoicing_frequency IN ('DAILY', 'WEEKLY', 'BI_WEEKLY', 'MONTHLY'))
);
