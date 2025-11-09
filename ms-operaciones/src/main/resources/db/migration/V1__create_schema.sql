-- Tabla de Rutas
CREATE TABLE IF NOT EXISTS rutas (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    solicitud_id UUID UNIQUE NOT NULL,
    distancia_km_plan NUMERIC(10,2),
    duracion_min_plan INTEGER,
    fecha_plan TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Tramos
CREATE TABLE IF NOT EXISTS tramos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ruta_id UUID NOT NULL REFERENCES rutas(id) ON DELETE CASCADE,
    orden INTEGER NOT NULL,
    origen_nombre VARCHAR(255),
    origen_lat NUMERIC(10,8),
    origen_lng NUMERIC(11,8),
    destino_nombre VARCHAR(255),
    destino_lat NUMERIC(10,8),
    destino_lng NUMERIC(11,8),
    distancia_km_plan NUMERIC(10,2),
    duracion_min_plan INTEGER,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    fecha_inicio_real TIMESTAMP,
    fecha_fin_real TIMESTAMP
);

-- Tabla de Asignaciones de Camiones
CREATE TABLE IF NOT EXISTS asignaciones_camiones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tramo_id UUID UNIQUE NOT NULL REFERENCES tramos(id) ON DELETE CASCADE,
    camion_id UUID NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmado BOOLEAN DEFAULT false
);

-- Tabla de Seguimiento de Tramos
CREATE TABLE IF NOT EXISTS seguimiento_tramos (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tramo_id UUID NOT NULL REFERENCES tramos(id) ON DELETE CASCADE,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    evento VARCHAR(50) NOT NULL,
    latitud NUMERIC(10,8),
    longitud NUMERIC(11,8),
    notas TEXT
);

-- Índices
CREATE INDEX idx_tramos_ruta ON tramos(ruta_id, orden);
CREATE INDEX idx_tramos_estado ON tramos(estado);
CREATE INDEX idx_asignaciones_camion ON asignaciones_camiones(camion_id);
CREATE INDEX idx_seguimiento_tramo ON seguimiento_tramos(tramo_id, timestamp);