CREATE TABLE IF NOT EXISTS clientes (
    id UUID PRIMARY KEY,
    razon_social VARCHAR(160) NOT NULL,
    cuit VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(160),
    telefono VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS contenedores (
    id UUID PRIMARY KEY,
    codigo VARCHAR(40) NOT NULL UNIQUE,
    peso_kg NUMERIC(12,2),
    volumen_m3 NUMERIC(12,2),
    tipo VARCHAR(40)
);

CREATE TABLE IF NOT EXISTS solicitudes (
    id UUID PRIMARY KEY,
    fecha_creacion TIMESTAMP NOT NULL,
    estado_actual VARCHAR(30) NOT NULL,
    cliente_id UUID NOT NULL REFERENCES clientes(id),
    contenedor_id UUID NOT NULL UNIQUE REFERENCES contenedores(id),
    tarifa_id UUID,
    costo_estimado NUMERIC(14,2),
    distancia_km_estimada INTEGER,
    eta_estimado TIMESTAMP,
    origen_nombre VARCHAR(160),
    origen_lat NUMERIC(9,6),
    origen_lng NUMERIC(9,6),
    destino_nombre VARCHAR(160),
    destino_lat NUMERIC(9,6),
    destino_lng NUMERIC(9,6),
    costo_real NUMERIC(14,2),
    tiempo_real_entrega TIMESTAMP
);

CREATE TABLE IF NOT EXISTS solicitud_estado_hist (
    id UUID PRIMARY KEY,
    solicitud_id UUID NOT NULL REFERENCES solicitudes(id) ON DELETE CASCADE,
    estado VARCHAR(30) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    observacion VARCHAR(250),
    actor_id UUID
);

INSERT INTO clientes (id, razon_social, cuit, email, telefono) VALUES
    ('8c2d08a0-1b2c-4d5e-9f60-7a8b9c0d1777', 'Logística Integral SA', '30-12345678-9', 'operaciones@logisticaintegral.com', '+54 11 4444-1000'),
    ('0f1e2d3c-4b5a-4978-8901-23456789e888', 'AgroExport SRL', '30-87654321-0', 'contacto@agroexport.com', '+54 341 455-9080');

INSERT INTO contenedores (id, codigo, peso_kg, volumen_m3, tipo) VALUES
    ('3c4d5e6f-7081-42a3-b4c5-d6e7f8a9b9c0', 'CONT-AR-0001', 12000.00, 33.20, '40HC'),
    ('7b8c9d0e-1f2a-43b4-95c6-d7e8f9010d11', 'CONT-AR-0002', 8500.00, 25.00, '20DV');

INSERT INTO solicitudes (id, fecha_creacion, estado_actual, cliente_id, contenedor_id, tarifa_id, costo_estimado, distancia_km_estimada, eta_estimado, origen_nombre, origen_lat, origen_lng, destino_nombre, destino_lat, destino_lng, costo_real, tiempo_real_entrega)
VALUES
    ('a1b2c3d4-e5f6-47a8-9b0c-d1e2f3a4b501', '2024-08-01 09:15:00', 'COMPLETADA', '8c2d08a0-1b2c-4d5e-9f60-7a8b9c0d1777', '3c4d5e6f-7081-42a3-b4c5-d6e7f8a9b9c0', '1a2b3c4d-5e6f-7081-92a3-b4c5d6e7f555', 450000.00, 950, '2024-08-05 18:00:00', 'Planta San Martín, CABA', -34.562300, -58.456700, 'Centro Logístico Córdoba', -31.420100, -64.188800, 470500.00, '2024-08-05 17:30:00'),
    ('bbccdde0-1122-43c4-95d6-e7f8091a2b02', '2024-08-10 11:30:00', 'PLANIFICADA', '0f1e2d3c-4b5a-4978-8901-23456789e888', '7b8c9d0e-1f2a-43b4-95c6-d7e8f9010d11', '5f4e3d2c-1b0a-4988-8766-554433221666', 265000.00, 600, '2024-08-12 20:00:00', 'Puerto Rosario', -32.951200, -60.666900, 'Parque Industrial Reconquista', -29.144300, -59.642000, NULL, NULL);

INSERT INTO solicitud_estado_hist (id, solicitud_id, estado, fecha_hora, observacion, actor_id)
VALUES
    ('c0d1e2f3-4455-4667-8888-99aabbccdd01', 'a1b2c3d4-e5f6-47a8-9b0c-d1e2f3a4b501', 'CREADA', '2024-08-01 09:15:00', 'Solicitud ingresada por el cliente', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0001'),
    ('d1e2f3a4-5566-4778-9999-aabbccddeeff', 'a1b2c3d4-e5f6-47a8-9b0c-d1e2f3a4b501', 'VALIDADA', '2024-08-01 12:00:00', 'Datos validados por operador comercial', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0002'),
    ('e2f3a4b5-6677-4889-aaaa-bbccddeeff00', 'a1b2c3d4-e5f6-47a8-9b0c-d1e2f3a4b501', 'PLANIFICADA', '2024-08-02 10:30:00', 'Ruta planificada y camión asignado', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0003'),
    ('f3a4b5c6-7788-499a-bbbb-ccddeeff0011', 'a1b2c3d4-e5f6-47a8-9b0c-d1e2f3a4b501', 'EN_CURSO', '2024-08-03 07:45:00', 'Camión salió del origen', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0004'),
    ('0a1b2c3d-8899-4aac-cccc-ddeeff001122', 'a1b2c3d4-e5f6-47a8-9b0c-d1e2f3a4b501', 'COMPLETADA', '2024-08-05 17:30:00', 'Entrega confirmada en destino', 'aaaaaaaa-bbbb-cccc-dddd-eeeeeeee0005'),
    ('1b2c3d4e-99aa-4bbd-dddd-eeff00112233', 'bbccdde0-1122-43c4-95d6-e7f8091a2b02', 'CREADA', '2024-08-10 11:30:00', 'Solicitud creada desde portal', 'bbbbbbbb-cccc-dddd-eeee-ffffffff0001'),
    ('2c3d4e5f-aaab-4cce-eeee-ff0011223344', 'bbccdde0-1122-43c4-95d6-e7f8091a2b02', 'VALIDADA', '2024-08-10 13:15:00', 'Información confirmada con el cliente', 'bbbbbbbb-cccc-dddd-eeee-ffffffff0002'),
    ('3d4e5f60-bbbc-4ddf-ffff-001122334455', 'bbccdde0-1122-43c4-95d6-e7f8091a2b02', 'PLANIFICADA', '2024-08-11 09:00:00', 'Tramo en coordinación con operaciones', 'bbbbbbbb-cccc-dddd-eeee-ffffffff0003');
