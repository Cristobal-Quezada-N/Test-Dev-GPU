-- ========== User Status ==========
INSERT INTO user_status (id, code, name) VALUES
(1, 'PENDING',  'Pendiente'),
(2, 'ACTIVE',   'Activo'),
(3, 'BANNED',   'Bloqueado');

-- ========== Loan Status ==========
INSERT INTO loan_status (id, code, name) VALUES
(1, 'ACCEPTED', 'Aceptado'),
(2, 'DENIED',   'Denegado'),
(3, 'RECEIVED', 'Recibido'),
(4, 'RETURNED', 'Retornado'),
(5, 'OVERDUE',  'Atrasado');

-- ========== Roles ==========
INSERT INTO role (id, code, name) VALUES
(1, 'ADMIN', 'Administrador'),
(2, 'USER', 'Usuario');

-- ========== Auth Factor Type ==========
INSERT INTO auth_factor_type (id, code, name) VALUES
(1, 'TOTP',     'TOTP'),
(2, 'REGISTER', 'Registro'),
(3, 'LOAN',     'Prestamo');

-- ========== Activation Token Status  ==========
INSERT INTO activation_token_status (id, code, name) VALUES
(1, 'PENDING',  'Pendiente'),
(2, 'USED',     'Usado'),
(3, 'EXPIRED',  'Expirado'),
(4, 'REVOKED',  'Revocado');
