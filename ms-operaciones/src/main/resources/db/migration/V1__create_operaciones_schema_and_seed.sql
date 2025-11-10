CREATE TABLE IF NOT EXISTS rutas (
    id UUID PRIMARY KEY,
    solicitud_id UUID NOT NULL UNIQUE,
    distancia_km_plan NUMERIC(10,2),
    duracion_min_plan INTEGER,
    fecha_plan TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS tramos (
    id UUID PRIMARY KEY,
    ruta_id UUID NOT NULL REFERENCES rutas(id) ON DELETE CASCADE,
    orden INTEGER NOT NULL,
    origen_nombre VARCHAR(160),
    origen_lat NUMERIC(9,6),
    origen_lng NUMERIC(9,6),
    destino_nombre VARCHAR(160),
    destino_lat NUMERIC(9,6),
    destino_lng NUMERIC(9,6),
    distancia_km_plan NUMERIC(10,2),
    duracion_min_plan INTEGER,
    estado VARCHAR(30) NOT NULL,
    fecha_inicio_real TIMESTAMP,
    fecha_fin_real TIMESTAMP
);

CREATE TABLE IF NOT EXISTS asignaciones_camiones (
    id UUID PRIMARY KEY,
    tramo_id UUID NOT NULL UNIQUE REFERENCES tramos(id) ON DELETE CASCADE,
    camion_id UUID NOT NULL,
    fecha_asignacion TIMESTAMP NOT NULL,
    confirmado BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS seguimiento_tramos (
    id UUID PRIMARY KEY,
    tramo_id UUID NOT NULL REFERENCES tramos(id) ON DELETE CASCADE,
    "timestamp" TIMESTAMP NOT NULL,
    evento VARCHAR(40) NOT NULL,
    latitud NUMERIC(9,6),
    longitud NUMERIC(9,6),
    notas VARCHAR(250)
);

INSERT INTO rutas (id, solicitud_id, distancia_km_plan, duracion_min_plan, fecha_plan)
VALUES
    ('f1e2d3c4-b5a6-47c8-9d0e-f1a2b3c4d701', 'a1b2c3d4-e5f6-47a8-9b0c-d1e2f3a4b501', 960.0, 960, '2024-08-02 08:00:00'),
    ('0c1d2e3f-4a5b-46c7-98d0-e1f2a3b4c703', 'bbccdde0-1122-43c4-95d6-e7f8091a2b02', 610.0, 720, '2024-08-11 08:30:00');

INSERT INTO tramos (id, ruta_id, orden, origen_nombre, origen_lat, origen_lng, destino_nombre, destino_lat, destino_lng, distancia_km_plan, duracion_min_plan, estado, fecha_inicio_real, fecha_fin_real)
VALUES
    ('d5c4b3a2-1f0e-4d3c-8b7a-6f5e4d3c2701', 'f1e2d3c4-b5a6-47c8-9d0e-f1a2b3c4d701', 1, 'Planta San Martín, CABA', -34.562300, -58.456700, 'Depósito Panamericana', -34.345600, -58.794500, 55.0, 90, 'COMPLETADO', '2024-08-03 06:30:00', '2024-08-03 08:15:00'),
    ('e6d5c4b3-2a1f-4e3d-9c8b-7a6f5e4d2802', 'f1e2d3c4-b5a6-47c8-9d0e-f1a2b3c4d701', 2, 'Depósito Panamericana', -34.345600, -58.794500, 'Centro Logístico Córdoba', -31.420100, -64.188800, 905.0, 870, 'COMPLETADO', '2024-08-03 09:00:00', '2024-08-05 17:10:00'),
    ('f7e6d5c4-3b2a-4f3e-8d9c-0b1a2c3d2903', '0c1d2e3f-4a5b-46c7-98d0-e1f2a3b4c703', 1, 'Puerto Rosario', -32.951200, -60.666900, 'Parque Industrial Reconquista', -29.144300, -59.642000, 610.0, 720, 'PENDIENTE', NULL, NULL);

INSERT INTO asignaciones_camiones (id, tramo_id, camion_id, fecha_asignacion, confirmado)
VALUES
    ('0d1c2b3a-4e5f-4678-8899-aabbccdde701', 'd5c4b3a2-1f0e-4d3c-8b7a-6f5e4d3c2701', 'b8f1b9c5-1c22-4b89-9a75-0193f1a0e111', '2024-08-02 14:00:00', TRUE),
    ('1e2d3c4b-5f60-4789-99aa-bbccddeef802', 'e6d5c4b3-2a1f-4e3d-9c8b-7a6f5e4d2802', 'b8f1b9c5-1c22-4b89-9a75-0193f1a0e111', '2024-08-02 14:00:00', TRUE),
    ('2f3e4d5c-6071-489a-aabb-ccddeeff0903', 'f7e6d5c4-3b2a-4f3e-8d9c-0b1a2c3d2903', 'c6e2d7f0-8b4c-4c3a-9b1b-6b2f5e2d2f22', '2024-08-11 07:00:00', FALSE);

INSERT INTO seguimiento_tramos (id, tramo_id, "timestamp", evento, latitud, longitud, notas)
VALUES
    ('301f2e3d-4c5b-46a7-88b9-a0b1c2d3e401', 'd5c4b3a2-1f0e-4d3c-8b7a-6f5e4d3c2701', '2024-08-03 06:45:00', 'INICIO', -34.562300, -58.456700, 'Unidad lista para cargar'),
    ('402e3d4c-5b6a-47a8-99ba-b1c2d3e4f502', 'd5c4b3a2-1f0e-4d3c-8b7a-6f5e4d3c2701', '2024-08-03 07:15:00', 'SALIDA_ORIGEN', -34.550000, -58.500000, 'Salida registrada en portería'),
    ('503d4c5b-6a79-48a9-aacc-c2d3e4f50603', 'd5c4b3a2-1f0e-4d3c-8b7a-6f5e4d3c2701', '2024-08-03 08:15:00', 'FIN', -34.345600, -58.794500, 'Arribo al depósito'),
    ('604c5b6a-7a89-49ba-abcd-d3e4f5071404', 'e6d5c4b3-2a1f-4e3d-9c8b-7a6f5e4d2802', '2024-08-03 09:15:00', 'SALIDA_DEPOSITO', -34.330000, -58.800000, 'Salida hacia Córdoba'),
    ('705d6c7b-8a9b-4ccd-bdde-eef001122405', 'e6d5c4b3-2a1f-4e3d-9c8b-7a6f5e4d2802', '2024-08-05 16:45:00', 'ARRIBO_DESTINO', -31.430000, -64.200000, 'Arribo a Córdoba confirmado'),
    ('806e7d8c-9bab-4dde-ceef-001122334506', 'e6d5c4b3-2a1f-4e3d-9c8b-7a6f5e4d2802', '2024-08-05 17:10:00', 'FIN', -31.420100, -64.188800, 'Entrega finalizada en destino');
