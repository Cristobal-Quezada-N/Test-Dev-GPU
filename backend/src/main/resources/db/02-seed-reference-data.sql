-- ========== User Status ==========
INSERT INTO user_status (code, name) VALUES
('PENDING',  'Pendiente'),
('ACTIVE',   'Activo'),
('BANNED',   'Bloqueado');

-- ========== Loan Status ==========
INSERT INTO loan_status (code, name) VALUES
('ACCEPTED', 'Aceptado'),
('DENIED',   'Denegado'),
('RECEIVED', 'Recibido'),
('RETURNED', 'Retornado'),
('OVERDUE',  'Atrasado');

-- ========== Roles ==========
INSERT INTO role (code, name) VALUES
('ADMIN', 'Administrador'),
('USER', 'Usuario');

-- ========== Auth Factor Type ==========
INSERT INTO auth_factor_type (code, name) VALUES
('TOTP',     'TOTP'),
('REGISTER', 'Registro'),
('LOAN',     'Prestamo'),
('EMAIL',     'Email');

-- ========== Activation Token Status  ==========
INSERT INTO activation_token_status (code, name) VALUES
('PENDING',  'Pendiente'),
('USED',     'Usado'),
('EXPIRED',  'Expirado'),
('REVOKED',  'Revocado');
