-- Vendor airport coverage (which vendor can supply which fuel at which airport)
-- Never edit this file after it has been applied.

CREATE TABLE vendor_airport_coverages (
    id                   BIGSERIAL   PRIMARY KEY,
    vendor_id            BIGINT      NOT NULL,
    airport_id           BIGINT      NOT NULL,
    fuel_type_id         BIGINT      NOT NULL,
    into_plane_agent_id  BIGINT,
    notes                TEXT,
    is_active            BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_vac_vendor           FOREIGN KEY (vendor_id)           REFERENCES vendors(id),
    CONSTRAINT fk_vac_airport          FOREIGN KEY (airport_id)          REFERENCES airports(id),
    CONSTRAINT fk_vac_fuel_type        FOREIGN KEY (fuel_type_id)        REFERENCES fuel_types(id),
    CONSTRAINT fk_vac_into_plane_agent FOREIGN KEY (into_plane_agent_id) REFERENCES into_plane_agents(id)
);

-- Partial unique index: when agent is present, enforce uniqueness on all four columns.
-- When agent is NULL, each vendor/airport/fuel combination is unique.
CREATE UNIQUE INDEX uq_vac_with_agent
    ON vendor_airport_coverages (vendor_id, airport_id, fuel_type_id, into_plane_agent_id)
    WHERE into_plane_agent_id IS NOT NULL;

CREATE UNIQUE INDEX uq_vac_without_agent
    ON vendor_airport_coverages (vendor_id, airport_id, fuel_type_id)
    WHERE into_plane_agent_id IS NULL;

CREATE INDEX idx_vac_vendor_id  ON vendor_airport_coverages (vendor_id);
CREATE INDEX idx_vac_airport_id ON vendor_airport_coverages (airport_id);
CREATE INDEX idx_vac_is_active  ON vendor_airport_coverages (is_active);
