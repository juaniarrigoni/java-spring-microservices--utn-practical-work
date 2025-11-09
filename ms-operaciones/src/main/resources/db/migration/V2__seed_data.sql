-- ============================================
-- SEED DATA PARA MS-OPERACIONES
-- ============================================

-- Ruta para la solicitud COMPLETADA (s1111111...)
INSERT INTO rutas (id, solicitud_id, distancia_km_plan, duracion_min_plan, fecha_plan)
VALUES 
    ('r1111111-1111-1111-1111-111111111111', 's1111111-1111-1111-1111-111111111111', 450.00, 360, '2025-10-15 14:30:00');

-- Tramos de la ruta completada (Córdoba → Buenos Aires)
INSERT INTO tramos (id, ruta_id, orden, origen_nombre, origen_lat, origen_lng, destino_nombre, destino_lat, destino_lng, distancia_km_plan, duracion_min_plan, estado, fecha_inicio_real, fecha_fin_real)
VALUES 
    ('t1111111-1111-1111-1111-111111111111', 'r1111111-1111-1111-1111-111111111111', 1, 
     'Córdoba Capital', -31.4201, -64.1888, 
     'Depósito Rosario', -32.9442, -60.6505, 
     200.00, 150, 'COMPLETADO', '2025-10-16 06:00:00', '2025-10-16 08:30:00'),
    
    ('t2222222-2222-2222-2222-222222222222', 'r1111111-1111-1111-1111-111111111111', 2, 
     'Depósito Rosario', -32.9442, -60.6505, 
     'Buenos Aires', -34.6037, -58.3816, 
     250.00, 210, 'COMPLETADO', '2025-10-16 10:00:00', '2025-10-17 18:30:00');

-- Asignaciones de camiones para la ruta completada
INSERT INTO asignaciones_camiones (id, tramo_id, camion_id, fecha_asignacion, confirmado)
VALUES 
    ('a1111111-1111-1111-1111-111111111111', 't1111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', '2025-10-15 15:00:00', true),
    ('a2222222-2222-2222-2222-222222222222', 't2222222-2222-2222-2222-222222222222', '22222222-2222-2222-2222-222222222222', '2025-10-15 15:00:00', true);

-- Seguimiento del primer tramo (COMPLETADO)
INSERT INTO seguimiento_tramos (tramo_id, timestamp, evento, latitud, longitud, notas)
VALUES 
    ('t1111111-1111-1111-1111-111111111111', '2025-10-16 06:00:00', 'INICIO', -31.4201, -64.1888, 'Inicio del transporte desde Córdoba'),
    ('t1111111-1111-1111-1111-111111111111', '2025-10-16 06:15:00', 'SALIDA_ORIGEN', -31.4350, -64.1650, 'Salida confirmada del origen'),
    ('t1111111-1111-1111-1111-111111111111', '2025-10-16 08:15:00', 'ARRIBO_DEPOSITO', -32.9442, -60.6505, 'Llegada al depósito de Rosario'),
    ('t1111111-1111-1111-1111-111111111111', '2025-10-16 08:30:00', 'FIN', -32.9442, -60.6505, 'Tramo completado');

-- Seguimiento del segundo tramo (COMPLETADO)
INSERT INTO seguimiento_tramos (tramo_id, timestamp, evento, latitud, longitud, notas)
VALUES 
    ('t2222222-2222-2222-2222-222222222222', '2025-10-16 10:00:00', 'INICIO', -32.9442, -60.6505, 'Inicio desde depósito Rosario'),
    ('t2222222-2222-2222-2222-222222222222', '2025-10-16 10:15:00', 'SALIDA_DEPOSITO', -32.9300, -60.6400, 'Salida del depósito'),
    ('t2222222-2222-2222-2222-222222222222', '2025-10-17 18:15:00', 'ARRIBO_DESTINO', -34.6037, -58.3816, 'Llegada a Buenos Aires'),
    ('t2222222-2222-2222-2222-222222222222', '2025-10-17 18:30:00', 'FIN', -34.6037, -58.3816, 'Entrega exitosa');

-- ============================================
-- Ruta para solicitud EN_CURSO (s2222222...)
-- ============================================
INSERT INTO rutas (id, solicitud_id, distancia_km_plan, duracion_min_plan, fecha_plan)
VALUES 
    ('r2222222-2222-2222-2222-222222222222', 's2222222-2222-2222-2222-222222222222', 320.00, 240, '2025-11-01 12:00:00');

-- Tramo en curso (Rosario → Córdoba)
INSERT INTO tramos (id, ruta_id, orden, origen_nombre, origen_lat, origen_lng, destino_nombre, destino_lat, destino_lng, distancia_km_plan, duracion_min_plan, estado, fecha_inicio_real)
VALUES 
    ('t3333333-3333-3333-3333-333333333333', 'r2222222-2222-2222-2222-222222222222', 1, 
     'Rosario', -32.9442, -60.6505, 
     'Córdoba Capital', -31.4201, -64.1888, 
     320.00, 240, 'EN_CURSO', '2025-11-02 07:00:00');

-- Asignación para el tramo en curso
INSERT INTO asignaciones_camiones (id, tramo_id, camion_id, fecha_asignacion, confirmado)
VALUES 
    ('a3333333-3333-3333-3333-333333333333', 't3333333-3333-3333-3333-333333333333', '33333333-3333-3333-3333-333333333333', '2025-11-01 13:00:00', true);

-- Seguimiento del tramo en curso (aún en tránsito)
INSERT INTO seguimiento_tramos (tramo_id, timestamp, evento, latitud, longitud, notas)
VALUES 
    ('t3333333-3333-3333-3333-333333333333', '2025-11-02 07:00:00', 'INICIO', -32.9442, -60.6505, 'Inicio del transporte'),
    ('t3333333-3333-3333-3333-333333333333', '2025-11-02 07:15:00', 'SALIDA_ORIGEN', -32.9300, -60.6300, 'Salida de Rosario'),
    ('t3333333-3333-3333-3333-333333333333', '2025-11-02 09:30:00', 'INCIDENTE', -32.5000, -62.0000, 'Parada técnica de 15 minutos');

-- ============================================
-- Ruta para solicitud PLANIFICADA (s3333333...)
-- ============================================
INSERT INTO rutas (id, solicitud_id, distancia_km_plan, duracion_min_plan, fecha_plan)
VALUES 
    ('r3333333-3333-3333-3333-333333333333', 's3333333-3333-3333-3333-333333333333', 850.00, 720, '2025-11-05 16:00:00');

-- Tramos planificados pero aún no iniciados (Buenos Aires → Mendoza)
INSERT INTO tramos (id, ruta_id, orden, origen_nombre, origen_lat, origen_lng, destino_nombre, destino_lat, destino_lng, distancia_km_plan, duracion_min_plan, estado)
VALUES 
    ('t4444444-4444-4444-4444-444444444444', 'r3333333-3333-3333-3333-333333333333', 1, 
     'Buenos Aires', -34.6037, -58.3816, 
     'Depósito Córdoba', -31.4201, -64.1888, 
     450.00, 360, 'PENDIENTE'),
    
    ('t5555555-5555-5555-5555-555555555555', 'r3333333-3333-3333-3333-333333333333', 2, 
     'Depósito Córdoba', -31.4201, -64.1888, 
     'Mendoza', -32.8895, -68.8458, 
     400.00, 360, 'PENDIENTE');

-- Asignaciones para tramos planificados
INSERT INTO asignaciones_camiones (id, tramo_id, camion_id, fecha_asignacion, confirmado)
VALUES 
    ('a4444444-4444-4444-4444-444444444444', 't4444444-4444-4444-4444-444444444444', '55555555-5555-5555-5555-555555555555', '2025-11-05 16:30:00', false),
    ('a5555555-5555-5555-5555-555555555555', 't5555555-5555-5555-5555-555555555555', '11111111-1111-1111-1111-111111111111', '2025-11-05 16:30:00', false);