-- ========== User Status ==========
INSERT INTO user_status (id, code, name) VALUES
(1, 'PENDING',  'Pendiente'),
(2, 'ACTIVE',   'Activo'),
(3, 'BANNED',   'Bloqueado');

-- ========== Loan Status ==========
INSERT INTO loan_status (id, code, name) VALUES
(1, 'RECEIVED', 'Recibido'),
(2, 'ACCEPTED', 'Aceptado'),
(3, 'DENIED',   'Denegado');

-- ========== Roles ==========
INSERT INTO role (id, code, name) VALUES
(1, 'ADMIN', 'Administrador'),
(2, 'USER', 'Usuario');

-- ========== Auth Factor Type ==========
INSERT INTO auth_factor_type (id, code, name) VALUES
(1, 'TOTP',     'TOTP'),
(2, 'REGISTER', 'Registro'),
(3, 'LOAN',     'Prestamo');
