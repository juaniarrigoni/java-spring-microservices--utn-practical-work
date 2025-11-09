-- ============================================
-- SEED DATA PARA MS-CATALOGOS
-- ============================================

-- Camiones de prueba
INSERT INTO camiones (id, patente, capacidad_kg, volumen_m3, tipo, consumo_combustible_km, costo_base_km, nombre_transportista, telefono_transportista, activo, disponible)
VALUES 
    ('11111111-1111-1111-1111-111111111111', 'AA123BB', 10000.00, 30.00, 'CAMION_GRANDE', 0.35, 2.50, 'Transportes Rodriguez', '+54-351-4567890', true, true),
    ('22222222-2222-2222-2222-222222222222', 'CC456DD', 8000.00, 25.00, 'CAMION_MEDIANO', 0.30, 2.00, 'Logística del Sur', '+54-351-4567891', true, true),
    ('33333333-3333-3333-3333-333333333333', 'EE789FF', 15000.00, 40.00, 'CAMION_EXTRA_GRANDE', 0.45, 3.00, 'Transportes del Norte', '+54-351-4567892', true, true),
    ('44444444-4444-4444-4444-444444444444', 'GG012HH', 5000.00, 20.00, 'CAMION_PEQUEÑO', 0.25, 1.50, 'Express Cargo', '+54-351-4567893', true, false),
    ('55555555-5555-5555-5555-555555555555', 'II345JJ', 12000.00, 35.00, 'CAMION_GRANDE', 0.38, 2.75, 'Trans-Argentina', '+54-351-4567894', true, true);

-- Depósitos estratégicos en Argentina
INSERT INTO depositos (id, nombre, direccion, latitud, longitud, activo, costo_estadia_diario)
VALUES 
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Depósito Central Córdoba', 'Av. Circunvalación km 8.5, Córdoba', -31.4201, -64.1888, true, 150.00),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Depósito Buenos Aires Norte', 'Panamericana km 32, Pilar', -34.4587, -58.9142, true, 200.00),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Depósito Rosario', 'Ruta A012 km 5, Rosario', -32.9442, -60.6505, true, 175.00),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Depósito Mendoza', 'Acceso Este s/n, Mendoza', -32.8895, -68.8458, true, 160.00),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Depósito Salta', 'Ruta 9 km 1650, Salta', -24.7859, -65.4117, true, 140.00);

-- Tarifas vigentes
INSERT INTO tarifas (id, nombre, precio_base, precio_km, precio_kg, precio_m3, vigencia_desde, vigencia_hasta, activa)
VALUES 
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Tarifa Estándar 2025', 5000.00, 15.00, 0.50, 100.00, '2025-01-01', '2025-12-31', true),
    ('99999999-9999-9999-9999-999999999999', 'Tarifa Express 2025', 8000.00, 20.00, 0.75, 150.00, '2025-01-01', '2025-12-31', true),
    ('88888888-8888-8888-8888-888888888888', 'Tarifa Económica 2025', 3500.00, 12.00, 0.40, 80.00, '2025-01-01', '2025-12-31', true);