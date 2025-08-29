-- ========== Roles ==========
INSERT INTO Role (id, code, name) VALUES
(1, 'ADMIN', 'Administrador'),
(2, 'USER', 'Usuario');

-- ========== User Status ==========
INSERT INTO User_status (id, code, name) VALUES
(1, 'PENDING', 'Pendiente'),
(2, 'ACTIVE', 'Activo'),
(3, 'BANNED', 'Bloqueado');

-- ========== Loan Status ==========
INSERT INTO Loan_Status (id, code, name) VALUES
(1, 'RECEIVED', 'Recibido'),
(2, 'ACCEPTED', 'Aceptado'),
(3, 'DENIED', 'Denegado');
