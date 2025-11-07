CREATE TABLE IF NOT EXISTS camiones (
    id SERIAL PRIMARY KEY,
    patente VARCHAR(16) NOT NULL UNIQUE,
    capacidad_kg NUMERIC(12,2) NOT NULL,
    volumen_m3 NUMERIC(12,2) NOT NULL,
    tipo VARCHAR(32) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS depositos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL UNIQUE,
    direccion VARCHAR(255) NOT NULL,
    lat NUMERIC(10,7) NOT NULL,
    lng NUMERIC(10,7) NOT NULL,
    costo_estadia_diario NUMERIC(12,2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS tarifas (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL UNIQUE,
    precio_base NUMERIC(12,2) NOT NULL,
    precio_km NUMERIC(12,2) NOT NULL,
    precio_kg NUMERIC(12,2) NOT NULL,
    precio_m3 NUMERIC(12,2) NOT NULL,
    vigencia_desde DATE NOT NULL,
    vigencia_hasta DATE NOT NULL,
    activa BOOLEAN NOT NULL DEFAULT TRUE
);
