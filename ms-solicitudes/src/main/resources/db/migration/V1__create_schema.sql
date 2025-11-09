-- Tabla de Clientes
CREATE TABLE IF NOT EXISTS clientes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    razon_social VARCHAR(255) NOT NULL,
    cuit VARCHAR(13) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE,
    telefono VARCHAR(50)
);

-- Tabla de Contenedores
CREATE TABLE IF NOT EXISTS contenedores (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    codigo VARCHAR(50) UNIQUE NOT NULL,
    peso_kg NUMERIC(10,2),
    volumen_m3 NUMERIC(10,2),
    tipo VARCHAR(50)
);

-- Tabla de Solicitudes
CREATE TABLE IF NOT EXISTS solicitudes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado_actual VARCHAR(50) NOT NULL,
    cliente_id UUID NOT NULL REFERENCES clientes(id),
    contenedor_id UUID NOT NULL REFERENCES contenedores(id),
    tarifa_id UUID,
    costo_estimado NUMERIC(12,2),
    distancia_km_estimada INTEGER,
    eta_estimado TIMESTAMP,
    origen_nombre VARCHAR(255),
    origen_lat NUMERIC(10,8),
    origen_lng NUMERIC(11,8),
    destino_nombre VARCHAR(255),
    destino_lat NUMERIC(10,8),
    destino_lng NUMERIC(11,8),
    costo_real NUMERIC(12,2),
    tiempo_real_entrega TIMESTAMP
);

-- Tabla de Historial de Estados
CREATE TABLE IF NOT EXISTS solicitud_estado_hist (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    solicitud_id UUID NOT NULL REFERENCES solicitudes(id),
    estado VARCHAR(50) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion TEXT,
    actor_id UUID
);

-- Índices
CREATE INDEX idx_solicitudes_cliente ON solicitudes(cliente_id);
CREATE INDEX idx_solicitudes_estado ON solicitudes(estado_actual);
CREATE INDEX idx_solicitudes_fecha ON solicitudes(fecha_creacion);
CREATE INDEX idx_historial_solicitud ON solicitud_estado_hist(solicitud_id, fecha_hora);