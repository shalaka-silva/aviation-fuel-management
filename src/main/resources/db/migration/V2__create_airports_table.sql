-- Milestone 1: Airport master data tables
-- Never edit this file after it has been applied.

CREATE TABLE airports (
    id          BIGSERIAL      PRIMARY KEY,
    icao_code   VARCHAR(4)     NOT NULL,
    iata_code   VARCHAR(3),
    name        VARCHAR(255)   NOT NULL,
    city        VARCHAR(100)   NOT NULL,
    country     VARCHAR(100)   NOT NULL,
    latitude    NUMERIC(19, 6) NOT NULL,
    longitude   NUMERIC(19, 6) NOT NULL,
    timezone    VARCHAR(100)   NOT NULL,
    elevation   INTEGER,
    is_active   BOOLEAN        NOT NULL DEFAULT TRUE,

    CONSTRAINT uq_airports_icao_code UNIQUE (icao_code)
);

CREATE INDEX idx_airports_icao_code ON airports (icao_code);
CREATE INDEX idx_airports_iata_code ON airports (iata_code) WHERE iata_code IS NOT NULL;
CREATE INDEX idx_airports_is_active  ON airports (is_active);

CREATE TABLE airport_import_records (
    id            BIGSERIAL     PRIMARY KEY,
    batch_id      VARCHAR(100)  NOT NULL,
    icao_code     VARCHAR(4),
    import_status VARCHAR(20)   NOT NULL,
    source_data   TEXT          NOT NULL,
    error_message TEXT,
    airport_id    BIGINT        REFERENCES airports(id),
    imported_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_airport_import_batch ON airport_import_records (batch_id);
