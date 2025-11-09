-- Tabla de Camiones
CREATE TABLE IF NOT EXISTS camiones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    patente VARCHAR(20) UNIQUE NOT NULL,
    capacidad_kg NUMERIC(10,2),
    volumen_m3 NUMERIC(10,2),
    tipo VARCHAR(50),
    consumo_combustible_km NUMERIC(10,2),
    costo_base_km NUMERIC(10,2),
    nombre_transportista VARCHAR(255),
    telefono_transportista VARCHAR(50),
    activo BOOLEAN DEFAULT true,
    disponible BOOLEAN DEFAULT true
);

-- Tabla de Depósitos
CREATE TABLE IF NOT EXISTS depositos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(500),
    latitud NUMERIC(10,8),
    longitud NUMERIC(11,8),
    activo BOOLEAN DEFAULT true,
    costo_estadia_diario NUMERIC(10,2)
);

-- Tabla de Tarifas
CREATE TABLE IF NOT EXISTS tarifas (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(255) NOT NULL,
    precio_base NUMERIC(10,2) NOT NULL,
    precio_km NUMERIC(10,2) NOT NULL,
    precio_kg NUMERIC(10,2) NOT NULL,
    precio_m3 NUMERIC(10,2) NOT NULL,
    vigencia_desde DATE NOT NULL,
    vigencia_hasta DATE NOT NULL,
    activa BOOLEAN DEFAULT true
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_camiones_activo ON camiones(activo);
CREATE INDEX idx_camiones_disponible ON camiones(disponible);
CREATE INDEX idx_depositos_activo ON depositos(activo);
CREATE INDEX idx_tarifas_vigencia ON tarifas(vigencia_desde, vigencia_hasta, activa);