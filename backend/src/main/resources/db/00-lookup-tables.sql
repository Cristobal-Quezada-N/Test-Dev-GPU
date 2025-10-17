-- ==========
-- Lookup Tables
-- ==========

DROP TABLE IF EXISTS user_status      CASCADE;
DROP TABLE IF EXISTS role             CASCADE;
DROP TABLE IF EXISTS loan_status      CASCADE;
DROP TABLE IF EXISTS auth_factor_type CASCADE;

CREATE TABLE user_status (
    id    SERIAL PRIMARY KEY,
    code  VARCHAR(20) NOT NULL UNIQUE,
    name  VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE loan_status (
    id    SERIAL PRIMARY KEY,
    code  VARCHAR(20) NOT NULL UNIQUE,
    name  VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE role (
    id    SERIAL PRIMARY KEY,
    code  VARCHAR(20) NOT NULL UNIQUE,
    name  VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE auth_factor_type (
    id    SERIAL PRIMARY KEY,
    code  VARCHAR(20) NOT NULL UNIQUE,
    name  VARCHAR(20) NOT NULL UNIQUE
);
