-- Vendor master data
-- Never edit this file after it has been applied.

CREATE TABLE vendors (
    id                  BIGSERIAL       PRIMARY KEY,
    name                VARCHAR(200)    NOT NULL,
    vendor_type         VARCHAR(20)     NOT NULL,
    main_supplier_name  VARCHAR(200),
    customer_number     VARCHAR(50),
    sales_rep_name      VARCHAR(200),
    sales_rep_email     VARCHAR(200),
    sales_rep_phone     VARCHAR(50),
    logistics_phone     VARCHAR(50),
    logistics_email     VARCHAR(200),
    is_active           BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_vendors_name UNIQUE (name),
    CONSTRAINT chk_vendors_type CHECK (vendor_type IN ('MAIN_SUPPLIER', 'BROKER'))
);

CREATE INDEX idx_vendors_is_active ON vendors (is_active);
