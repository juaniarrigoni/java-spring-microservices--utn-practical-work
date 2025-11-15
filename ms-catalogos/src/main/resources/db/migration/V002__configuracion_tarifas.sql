-- V002: Configuración de Tarifas Dinámicas
-- Implementa: 
-- - Configuración general de tarifas gestionable sin recompilar
-- - Tarifas variables por volumen del contenedor (regla de negocio)

-- ============================================
-- Tabla: configuracion_tarifas
-- ============================================
-- Almacena la configuración general del sistema de tarifas.
-- Solo puede haber una configuración activa a la vez.

CREATE TABLE configuracion_tarifas (
    id UUID PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    precio_litro_combustible NUMERIC(10, 2) NOT NULL CHECK (precio_litro_combustible > 0),
    cargo_gestion_por_tramo NUMERIC(10, 2) NOT NULL CHECK (cargo_gestion_por_tramo > 0),
    velocidad_promedio_km_h NUMERIC(5, 2) NOT NULL CHECK (velocidad_promedio_km_h > 0),
    costo_estadia_diario_default NUMERIC(10, 2) NOT NULL CHECK (costo_estadia_diario_default > 0),
    activa BOOLEAN NOT NULL DEFAULT true,
    vigencia_desde TIMESTAMP,
    vigencia_hasta TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_modificacion TIMESTAMP
);

-- ============================================
-- Tabla: tarifas_por_volumen
-- ============================================
-- Almacena las tarifas variables según el volumen del contenedor.
-- Define rangos de volumen (min, max) y el costo base por km correspondiente.
-- Implementa la regla: "El costo por km depende del volumen del contenedor"

CREATE TABLE tarifas_por_volumen (
    id UUID PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    volumen_min_m3 NUMERIC(10, 2) NOT NULL,
    volumen_max_m3 NUMERIC(10, 2), -- NULL = sin límite superior
    costo_base_km NUMERIC(10, 2) NOT NULL CHECK (costo_base_km > 0),
    activa BOOLEAN NOT NULL DEFAULT true,
    orden_prioridad INTEGER NOT NULL DEFAULT 0,
    vigencia_desde TIMESTAMP,
    vigencia_hasta TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_modificacion TIMESTAMP,
    CONSTRAINT chk_rango_volumen CHECK (volumen_max_m3 IS NULL OR volumen_min_m3 < volumen_max_m3)
);

-- ============================================
-- Índices para optimizar consultas
-- ============================================

-- Índice para búsqueda de configuración activa
CREATE INDEX idx_configuracion_tarifas_activa ON configuracion_tarifas(activa) WHERE activa = true;

-- Índices para búsqueda de tarifas por volumen
CREATE INDEX idx_tarifas_volumen_activa ON tarifas_por_volumen(activa) WHERE activa = true;
CREATE INDEX idx_tarifas_volumen_rango ON tarifas_por_volumen(volumen_min_m3, volumen_max_m3);
CREATE INDEX idx_tarifas_volumen_orden ON tarifas_por_volumen(orden_prioridad);
CREATE INDEX idx_tarifas_volumen_vigencia ON tarifas_por_volumen(vigencia_desde, vigencia_hasta);

-- ============================================
-- Datos iniciales
-- ============================================

-- Configuración principal del sistema
INSERT INTO configuracion_tarifas (
    id, 
    nombre, 
    descripcion,
    precio_litro_combustible,
    cargo_gestion_por_tramo,
    velocidad_promedio_km_h,
    costo_estadia_diario_default,
    activa,
    vigencia_desde,
    fecha_creacion
) VALUES (
    'a1b2c3d4-e5f6-4a5b-8c7d-9e0f1a2b3c4d',
    'Configuración Principal 2025',
    'Configuración de tarifas vigente para el año 2025',
    150.00,  -- precio litro combustible en pesos
    2500.00, -- cargo de gestión por cada tramo
    60.00,   -- velocidad promedio en km/h
    500.00,  -- costo de estadía diario por defecto
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Tarifas por volumen (implementa regla de negocio: costo variable según volumen)

-- Rango 1: Contenedores pequeños (0-20 m³)
INSERT INTO tarifas_por_volumen (
    id,
    nombre,
    descripcion,
    volumen_min_m3,
    volumen_max_m3,
    costo_base_km,
    activa,
    orden_prioridad,
    vigencia_desde,
    fecha_creacion
) VALUES (
    'd1e2f3a4-b5c6-4d7e-8f9a-0b1c2d3e4f5a',
    'Pequeño (0-20 m³)',
    'Tarifas para contenedores pequeños hasta 20 metros cúbicos',
    0.00,
    20.00,
    80.00,  -- $80 por km
    true,
    10,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Rango 2: Contenedores medianos (20-50 m³)
INSERT INTO tarifas_por_volumen (
    id,
    nombre,
    descripcion,
    volumen_min_m3,
    volumen_max_m3,
    costo_base_km,
    activa,
    orden_prioridad,
    vigencia_desde,
    fecha_creacion
) VALUES (
    'e2f3a4b5-c6d7-4e8f-9a0b-1c2d3e4f5a6b',
    'Mediano (20-50 m³)',
    'Tarifas para contenedores medianos de 20 a 50 metros cúbicos',
    20.00,
    50.00,
    95.00,  -- $95 por km
    true,
    20,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Rango 3: Contenedores grandes (50-80 m³)
INSERT INTO tarifas_por_volumen (
    id,
    nombre,
    descripcion,
    volumen_min_m3,
    volumen_max_m3,
    costo_base_km,
    activa,
    orden_prioridad,
    vigencia_desde,
    fecha_creacion
) VALUES (
    'f3a4b5c6-d7e8-4f9a-0b1c-2d3e4f5a6b7c',
    'Grande (50-80 m³)',
    'Tarifas para contenedores grandes de 50 a 80 metros cúbicos',
    50.00,
    80.00,
    110.00,  -- $110 por km
    true,
    30,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Rango 4: Contenedores extra grandes (80+ m³)
INSERT INTO tarifas_por_volumen (
    id,
    nombre,
    descripcion,
    volumen_min_m3,
    volumen_max_m3,
    costo_base_km,
    activa,
    orden_prioridad,
    vigencia_desde,
    fecha_creacion
) VALUES (
    'a4b5c6d7-e8f9-4a0b-1c2d-3e4f5a6b7c8d',
    'Extra Grande (80+ m³)',
    'Tarifas para contenedores extra grandes de más de 80 metros cúbicos',
    80.00,
    NULL,   -- Sin límite superior
    130.00,  -- $130 por km
    true,
    40,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- ============================================
-- Comentarios descriptivos
-- ============================================

COMMENT ON TABLE configuracion_tarifas IS 'Configuración general de tarifas del sistema. Solo puede haber una configuración activa.';
COMMENT ON TABLE tarifas_por_volumen IS 'Tarifas variables según el volumen del contenedor. Implementa la regla: el costo por km depende del volumen.';

COMMENT ON COLUMN configuracion_tarifas.precio_litro_combustible IS 'Precio del litro de combustible en pesos';
COMMENT ON COLUMN configuracion_tarifas.cargo_gestion_por_tramo IS 'Cargo fijo de gestión por cada tramo de la ruta';
COMMENT ON COLUMN configuracion_tarifas.velocidad_promedio_km_h IS 'Velocidad promedio en km/h para calcular duraciones estimadas';
COMMENT ON COLUMN configuracion_tarifas.costo_estadia_diario_default IS 'Costo diario de estadía en depósitos por defecto';

COMMENT ON COLUMN tarifas_por_volumen.volumen_min_m3 IS 'Volumen mínimo en metros cúbicos (inclusive)';
COMMENT ON COLUMN tarifas_por_volumen.volumen_max_m3 IS 'Volumen máximo en metros cúbicos (exclusive). NULL = sin límite superior';
COMMENT ON COLUMN tarifas_por_volumen.costo_base_km IS 'Costo base por kilómetro en pesos';
COMMENT ON COLUMN tarifas_por_volumen.orden_prioridad IS 'Orden de prioridad para resolver conflictos en rangos solapados';
