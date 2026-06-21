-- Into-plane agent master data
-- Never edit this file after it has been applied.

CREATE TABLE into_plane_agents (
    id                       BIGSERIAL       PRIMARY KEY,
    code                     VARCHAR(50)     NOT NULL,
    name                     VARCHAR(200)    NOT NULL,
    operating_hours          VARCHAR(100),
    minimum_notice_required  VARCHAR(100),
    ramps_serviced           VARCHAR(300),
    contact_email            VARCHAR(200),
    contact_phone            VARCHAR(50),
    is_active                BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at               TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

    CONSTRAINT uq_into_plane_agents_code UNIQUE (code)
);

CREATE INDEX idx_into_plane_agents_is_active ON into_plane_agents (is_active);
