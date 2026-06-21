-- Fuel type master data
-- Never edit this file after it has been applied.

CREATE TABLE fuel_types (
    id                      BIGSERIAL       PRIMARY KEY,
    code                    VARCHAR(20)     NOT NULL,
    name                    VARCHAR(100)    NOT NULL,
    default_unit_of_measure VARCHAR(10)     NOT NULL,
    is_active               BOOLEAN         NOT NULL DEFAULT TRUE,

    CONSTRAINT uq_fuel_types_code UNIQUE (code),
    CONSTRAINT chk_fuel_types_uom CHECK (default_unit_of_measure IN ('USG', 'L', 'KG', 'MT'))
);

INSERT INTO fuel_types (code, name, default_unit_of_measure) VALUES
    ('JET_A',      'Jet A',                        'USG'),
    ('JET_A1',     'Jet A-1',                      'USG'),
    ('AVGAS_100LL','Aviation Gasoline 100LL',       'USG'),
    ('AVGAS_80',   'Aviation Gasoline 80',          'USG');
