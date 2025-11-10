CREATE TABLE IF NOT EXISTS camiones (
    id UUID PRIMARY KEY,
    patente VARCHAR(20) NOT NULL UNIQUE,
    capacidad_kg NUMERIC(12,2),
    volumen_m3 NUMERIC(12,2),
    tipo VARCHAR(50),
    consumo_combustible_km NUMERIC(10,2),
    costo_base_km NUMERIC(10,2),
    nombre_transportista VARCHAR(120),
    telefono_transportista VARCHAR(40),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    disponible BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS depositos (
    id UUID PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    direccion VARCHAR(200),
    latitud NUMERIC(9,6),
    longitud NUMERIC(9,6),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    costo_estadia_diario NUMERIC(12,2)
);

CREATE TABLE IF NOT EXISTS tarifas (
    id UUID PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    precio_base NUMERIC(12,2),
    precio_km NUMERIC(12,2),
    precio_kg NUMERIC(12,2),
    precio_m3 NUMERIC(12,2),
    vigencia_desde DATE NOT NULL,
    vigencia_hasta DATE,
    activa BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO camiones (id, patente, capacidad_kg, volumen_m3, tipo, consumo_combustible_km, costo_base_km, nombre_transportista, telefono_transportista, activo, disponible)
VALUES
    ('b8f1b9c5-1c22-4b89-9a75-0193f1a0e111', 'AA123BB', 25000, 60, 'Semirremolque', 0.32, 95.50, 'Transporte Sur S.A.', '+54 11 5555-1000', TRUE, TRUE),
    ('c6e2d7f0-8b4c-4c3a-9b1b-6b2f5e2d2f22', 'CC456DD', 18000, 45, 'Chasis', 0.28, 82.75, 'Logística Norte', '+54 351 555-2200', TRUE, TRUE);

INSERT INTO depositos (id, nombre, direccion, latitud, longitud, activo, costo_estadia_diario)
VALUES
    ('6b3f9fdd-5e3c-47b1-b3ad-5e74af41f333', 'Depósito Panamericana', 'Panamericana Km 45, Escobar, Buenos Aires', -34.345600, -58.794500, TRUE, 14500.00),
    ('9d6a3e3d-8a6d-4c4f-8d8d-928ddf44f444', 'Depósito Rosario', 'Av. Circunvalación 1234, Rosario, Santa Fe', -32.944200, -60.650500, TRUE, 11800.00);

INSERT INTO tarifas (id, nombre, precio_base, precio_km, precio_kg, precio_m3, vigencia_desde, vigencia_hasta, activa)
VALUES
    ('1a2b3c4d-5e6f-7081-92a3-b4c5d6e7f555', 'Tarifa Nacional Estándar', 15000.00, 120.00, 2.50, 35.00, '2024-01-01', NULL, TRUE),
    ('5f4e3d2c-1b0a-4988-8766-554433221666', 'Tarifa Refrigerada', 22000.00, 150.00, 3.80, 45.00, '2024-01-01', NULL, TRUE);
