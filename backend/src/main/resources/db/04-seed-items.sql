-- ========== Items  ==========
INSERT INTO item (name, description, category, min_people) VALUES
('Plumones Permanentes', 'Set de 12 plumones permanentes de colores variados, marca Sharpie', 'PENS', 1),
('Calculadora Científica Casio FX-991', 'Calculadora científica avanzada con 417 funciones', 'CALCULATOR', 1),
('Tabla Periódica Plastificada', 'Tabla periódica de los elementos tamaño poster (90x60cm)', 'LECTURE_MATERIAL', 1),
('Laptop HP ProBook 450', 'Laptop HP ProBook i5, 8GB RAM, 256GB SSD, Windows 11 Pro', 'LAPTOP', 1),
('Laptop Dell Latitude 5420', 'Laptop Dell i7, 16GB RAM, 512GB SSD, Windows 11 Pro', 'LAPTOP', 1),
('Mesa Ping Pong Profesional', 'Mesa de ping pong profesional plegable marca Stiga, incluye red y raquetas', 'GAME', 2),
('Mesa de Futbolito', 'Mesa de futbolito/taca-taca tamaño estándar, 6 jugadores por lado', 'GAME', 4);

-- ========== Items Copies ==========
-- COPIAS: Plumones Permanentes (5 sets)
INSERT INTO item_copy (item_id, copy_number, condition, status, acquisition_date, notes, created_at) VALUES
(1, 'PLUM-001', 'GOOD', 'AVAILABLE', '2024-01-15', 'Set completo de 12 colores', NOW()),
(1, 'PLUM-002', 'GOOD', 'AVAILABLE', '2024-01-15', 'Set completo de 12 colores', NOW()),
(1, 'PLUM-003', 'FAIR', 'AVAILABLE', '2024-01-15', 'Faltan 2 plumones (negro y rojo)', NOW()),
(1, 'PLUM-004', 'GOOD', 'LOANED', '2024-02-10', 'Set completo de 12 colores', NOW()),
(1, 'PLUM-005', 'DAMAGED', 'MAINTENANCE', '2024-01-15', 'Estuche roto, plumones intactos', NOW());

-- COPIAS: Calculadora Casio (8 unidades)
INSERT INTO item_copy (item_id, copy_number, condition, status, acquisition_date, notes, created_at) VALUES
(2, 'CALC-CAS-001', 'GOOD', 'AVAILABLE', '2023-03-10', 'Incluye estuche y manual', NOW()),
(2, 'CALC-CAS-002', 'GOOD', 'AVAILABLE', '2023-03-10', 'Incluye estuche y manual', NOW()),
(2, 'CALC-CAS-003', 'GOOD', 'LOANED', '2023-03-10', 'Incluye estuche y manual', NOW()),
(2, 'CALC-CAS-004', 'FAIR', 'AVAILABLE', '2023-03-10', 'Pantalla con rayón pequeño, funciona bien', NOW()),
(2, 'CALC-CAS-005', 'GOOD', 'AVAILABLE', '2023-08-20', 'Nueva adquisición', NOW()),
(2, 'CALC-CAS-006', 'GOOD', 'LOANED', '2023-08-20', 'Nueva adquisición', NOW()),
(2, 'CALC-CAS-007', 'DAMAGED', 'MAINTENANCE', '2023-03-10', 'Botones desgastados, necesita limpieza', NOW()),
(2, 'CALC-CAS-008', 'LOST', 'RETIRED', '2023-03-10', 'Reportada como perdida en Sep-2024', NOW());

-- COPIAS: Tabla Periódica (10 unidades)
INSERT INTO item_copy (item_id, copy_number, condition, status, acquisition_date, notes, created_at) VALUES
(3, 'TABLA-001', 'GOOD', 'AVAILABLE', '2024-02-01', 'Plastificada brillante', NOW()),
(3, 'TABLA-002', 'GOOD', 'AVAILABLE', '2024-02-01', 'Plastificada brillante', NOW()),
(3, 'TABLA-003', 'GOOD', 'LOANED', '2024-02-01', 'Plastificada brillante', NOW()),
(3, 'TABLA-004', 'GOOD', 'LOANED', '2024-02-01', 'Plastificada brillante', NOW()),
(3, 'TABLA-005', 'FAIR', 'AVAILABLE', '2024-02-01', 'Esquina superior derecha doblada', NOW()),
(3, 'TABLA-006', 'GOOD', 'AVAILABLE', '2024-02-01', 'Plastificada brillante', NOW()),
(3, 'TABLA-007', 'GOOD', 'AVAILABLE', '2024-02-01', 'Plastificada brillante', NOW()),
(3, 'TABLA-008', 'GOOD', 'LOANED', '2024-02-01', 'Plastificada brillante', NOW()),
(3, 'TABLA-009', 'DAMAGED', 'RETIRED', '2024-02-01', 'Plastificado despegado', NOW()),
(3, 'TABLA-010', 'GOOD', 'AVAILABLE', '2024-06-10', 'Nueva adquisición', NOW());

-- COPIAS: Laptop HP ProBook (6 unidades)
INSERT INTO item_copy (item_id, copy_number, condition, status, acquisition_date, notes, created_at) VALUES
(4, 'LAP-HP-001', 'GOOD', 'AVAILABLE', '2023-01-10', 'Incluye cargador y mouse inalámbrico', NOW()),
(4, 'LAP-HP-002', 'GOOD', 'LOANED', '2023-01-10', 'Incluye cargador y mouse inalámbrico', NOW()),
(4, 'LAP-HP-003', 'GOOD', 'LOANED', '2023-01-10', 'Incluye cargador y mouse inalámbrico', NOW()),
(4, 'LAP-HP-004', 'FAIR', 'AVAILABLE', '2023-01-10', 'Batería con 60% de capacidad original', NOW()),
(4, 'LAP-HP-005', 'GOOD', 'LOANED', '2023-07-20', 'Nueva adquisición', NOW()),
(4, 'LAP-HP-006', 'DAMAGED', 'MAINTENANCE', '2023-01-10', 'Disco duro con sectores dañados, en revisión', NOW());

-- COPIAS: Laptop Dell Latitude (4 unidades)
INSERT INTO item_copy (item_id, copy_number, condition, status, acquisition_date, notes, created_at) VALUES
(5, 'LAP-DELL-001', 'GOOD', 'AVAILABLE', '2024-01-05', 'Incluye cargador, mouse y mochila', NOW()),
(5, 'LAP-DELL-002', 'GOOD', 'LOANED', '2024-01-05', 'Incluye cargador, mouse y mochila', NOW()),
(5, 'LAP-DELL-003', 'GOOD', 'AVAILABLE', '2024-01-05', 'Incluye cargador, mouse y mochila', NOW()),
(5, 'LAP-DELL-004', 'GOOD', 'LOANED', '2024-01-05', 'Incluye cargador, mouse y mochila', NOW());

-- COPIAS: Mesa Ping Pong (2 unidades)
INSERT INTO item_copy (item_id, copy_number, condition, status, acquisition_date, notes, created_at) VALUES
(6, 'PING-001', 'GOOD', 'AVAILABLE', '2023-05-15', 'Ubicada en sala de juegos piso 2, incluye 4 raquetas y 6 pelotas', NOW()),
(6, 'PING-002', 'FAIR', 'AVAILABLE', '2023-05-15', 'Ubicada en patio exterior, pintura desgastada pero funcional', NOW());

-- COPIAS: Taca Taca (3 unidades)
INSERT INTO item_copy (item_id, copy_number, condition, status, acquisition_date, notes, created_at) VALUES
(7, 'TACA-001', 'GOOD', 'AVAILABLE', '2023-06-01', 'Ubicada en sala de juegos piso 1, incluye 3 pelotas de repuesto', NOW()),
(7, 'TACA-002', 'GOOD', 'AVAILABLE', '2023-06-01', 'Ubicada en sala de juegos piso 2, incluye 3 pelotas de repuesto', NOW()),
(7, 'TACA-003', 'DAMAGED', 'MAINTENANCE', '2023-06-01', 'Barra de portero atascada, en reparación', NOW());

-- Resetear secuencias
ALTER TABLE item ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM item);
ALTER TABLE item_copy ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM item_copy);
