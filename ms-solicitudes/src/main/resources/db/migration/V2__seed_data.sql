-- ============================================
-- SEED DATA PARA MS-SOLICITUDES
-- ============================================

-- Clientes
INSERT INTO clientes (id, razon_social, cuit, email, telefono)
VALUES 
    ('c1111111-1111-1111-1111-111111111111', 'Aceros del Centro S.A.', '30-12345678-9', 'contacto@aceroscentro.com.ar', '+54-351-4231234'),
    ('c2222222-2222-2222-2222-222222222222', 'Alimentos La Pampa S.R.L.', '30-23456789-0', 'ventas@alimentospampa.com.ar', '+54-351-4232345'),
    ('c3333333-3333-3333-3333-333333333333', 'Textiles Rosario S.A.', '30-34567890-1', 'operaciones@textilesrosario.com.ar', '+54-341-4243456'),
    ('c4444444-4444-4444-4444-444444444444', 'Construcciones del Sur', '30-45678901-2', 'info@construccionessur.com.ar', '+54-261-4254567'),
    ('c5555555-5555-5555-5555-555555555555', 'Electrónica Argentina S.A.', '30-56789012-3', 'logistica@electronica.com.ar', '+54-11-42656789');

-- Contenedores
INSERT INTO contenedores (id, codigo, peso_kg, volumen_m3, tipo)
VALUES 
    ('00000001-0000-0000-0000-000000000001', 'CONT-20-001', 5000.00, 18.50, 'CONTENEDOR_20'),
    ('00000002-0000-0000-0000-000000000002', 'CONT-20-002', 4800.00, 18.00, 'CONTENEDOR_20'),
    ('00000003-0000-0000-0000-000000000003', 'CONT-40-001', 9500.00, 38.00, 'CONTENEDOR_40'),
    ('00000004-0000-0000-0000-000000000004', 'CONT-40-002', 9200.00, 37.50, 'CONTENEDOR_40'),
    ('00000005-0000-0000-0000-000000000005', 'CONT-40HC-001', 12000.00, 45.00, 'CONTENEDOR_40_HC'),
    ('00000006-0000-0000-0000-000000000006', 'CONT-20-003', 5200.00, 19.00, 'CONTENEDOR_20'),
    ('00000007-0000-0000-0000-000000000007', 'CONT-40-003', 9800.00, 38.50, 'CONTENEDOR_40');

-- Solicitudes de ejemplo
INSERT INTO solicitudes (
    id, fecha_creacion, estado_actual, cliente_id, contenedor_id, 
    tarifa_id, costo_estimado, distancia_km_estimada,
    origen_nombre, origen_lat, origen_lng,
    destino_nombre, destino_lat, destino_lng
)
VALUES 
    -- Solicitud COMPLETADA
    (
        's1111111-1111-1111-1111-111111111111',
        '2025-10-15 10:30:00',
        'COMPLETADA',
        'c1111111-1111-1111-1111-111111111111',
        '00000001-0000-0000-0000-000000000001',
        'ffffffff-ffff-ffff-ffff-ffffffffffff',
        25000.00,
        450,
        'Córdoba Capital', -31.4201, -64.1888,
        'Buenos Aires', -34.6037, -58.3816
    ),
    -- Solicitud EN_CURSO
    (
        's2222222-2222-2222-2222-222222222222',
        '2025-11-01 08:00:00',
        'EN_CURSO',
        'c2222222-2222-2222-2222-222222222222',
        '00000003-0000-0000-0000-000000000003',
        'ffffffff-ffff-ffff-ffff-ffffffffffff',
        18500.00,
        320,
        'Rosario', -32.9442, -60.6505,
        'Córdoba Capital', -31.4201, -64.1888
    ),
    -- Solicitud PLANIFICADA
    (
        's3333333-3333-3333-3333-333333333333',
        '2025-11-05 14:20:00',
        'PLANIFICADA',
        'c3333333-3333-3333-3333-333333333333',
        '00000005-0000-0000-0000-000000000005',
        '99999999-9999-9999-9999-999999999999',
        42000.00,
        850,
        'Buenos Aires', -34.6037, -58.3816,
        'Mendoza', -32.8895, -68.8458
    ),
    -- Solicitud VALIDADA
    (
        's4444444-4444-4444-4444-444444444444',
        '2025-11-08 09:15:00',
        'VALIDADA',
        'c4444444-4444-4444-4444-444444444444',
        '00000002-0000-0000-0000-000000000002',
        '88888888-8888-8888-8888-888888888888',
        15200.00,
        280,
        'Córdoba Capital', -31.4201, -64.1888,
        'Rosario', -32.9442, -60.6505
    ),
    -- Solicitud CREADA
    (
        's5555555-5555-5555-5555-555555555555',
        '2025-11-09 11:45:00',
        'CREADA',
        'c5555555-5555-5555-5555-555555555555',
        '00000006-0000-0000-0000-000000000006',
        NULL,
        NULL,
        NULL,
        'Salta', -24.7859, -65.4117,
        'Buenos Aires', -34.6037, -58.3816
    );

-- Historial de estados para la solicitud completada
INSERT INTO solicitud_estado_hist (solicitud_id, estado, fecha_hora, observacion)
VALUES 
    ('s1111111-1111-1111-1111-111111111111', 'CREADA', '2025-10-15 10:30:00', 'Solicitud creada por el cliente'),
    ('s1111111-1111-1111-1111-111111111111', 'VALIDADA', '2025-10-15 11:00:00', 'Validación exitosa de datos'),
    ('s1111111-1111-1111-1111-111111111111', 'PLANIFICADA', '2025-10-15 14:30:00', 'Ruta planificada y camión asignado'),
    ('s1111111-1111-1111-1111-111111111111', 'EN_CURSO', '2025-10-16 06:00:00', 'Inicio del transporte'),
    ('s1111111-1111-1111-1111-111111111111', 'COMPLETADA', '2025-10-17 18:30:00', 'Entrega exitosa en destino');

-- Historial para solicitud en curso
INSERT INTO solicitud_estado_hist (solicitud_id, estado, fecha_hora, observacion)
VALUES 
    ('s2222222-2222-2222-2222-222222222222', 'CREADA', '2025-11-01 08:00:00', 'Solicitud creada'),
    ('s2222222-2222-2222-2222-222222222222', 'VALIDADA', '2025-11-01 09:15:00', 'Datos validados'),
    ('s2222222-2222-2222-2222-222222222222', 'PLANIFICADA', '2025-11-01 12:00:00', 'Planificación completada'),
    ('s2222222-2222-2222-2222-222222222222', 'EN_CURSO', '2025-11-02 07:00:00', 'Transporte iniciado');