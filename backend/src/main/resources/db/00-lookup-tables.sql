-- ==========
-- Lookup Tables
-- ==========

DROP TABLE IF EXISTS User_status        CASCADE;
DROP TABLE IF EXISTS Role               CASCADE;
DROP TABLE IF EXISTS Loan_Status        CASCADE;

DROP TYPE IF EXISTS auth_factor_type    CASCADE;

CREATE TYPE auth_factor_type as ENUM (
    'TOTP',
    'REGISTER',
    'LOAN'
);

CREATE TABLE User_status (
    id    SERIAL PRIMARY KEY,
    code  VARCHAR(50) NOT NULL UNIQUE,
    name  VARCHAR(20) NOT NULL
);

CREATE TABLE Loan_Status (
    id    SERIAL PRIMARY KEY,
    code  VARCHAR(50) NOT NULL UNIQUE,
    name  VARCHAR(20) NOT NULL
);

CREATE TABLE Role (
    id    SERIAL PRIMARY KEY,
    code  VARCHAR(50) NOT NULL UNIQUE,
    name  VARCHAR(20) NOT NULL
);
