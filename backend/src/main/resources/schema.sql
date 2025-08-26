-- ==========
-- Limpieza (debug)
-- ==========

DROP TABLE IF EXISTS User_status    CASCADE;
DROP TABLE IF EXISTS Role           CASCADE;
DROP TABLE IF EXISTS Item           CASCADE;
DROP TABLE IF EXISTS App_user       CASCADE;
DROP TABLE IF EXISTS Auth_factor    CASCADE;
DROP TABLE IF EXISTS Loan           CASCADE;
DROP TABLE IF EXISTS Loan_Status    CASCADE;

CREATE TYPE auth_factor_type as ENUM (
    'totp'
);

CREATE TABLE User_status (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50),
    name VARCHAR(20)
);

CREATE TABLE Role (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50),
    name VARCHAR(20)
);

CREATE TABLE Item (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE App_user (
    id VARCHAR(50) PRIMARY KEY,
    role_id INTEGER NOT NULL REFERENCES Role(id),
    status_id INTEGER NOT NULL REFERENCES User_status(id),
    email VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);

CREATE TABLE Auth_factor (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL REFERENCES App_user(id),
    type auth_factor_type NOT NULL,
    used BOOLEAN NOT NULL,
    creation_date DATE NOT NULL,
    expiration_date DATE NOT NULL
);

CREATE TABLE Loan (
    id SERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL REFERENCES App_user(id),
    item_id INTEGER NOT NULL,
    status_id INTEGER NOT NULL,
    date DATE NOT NULL,
    deadline DATE NOT NULL
);

CREATE TABLE Loan_Status (
    id SERIAL PRIMARY KEY,
    code VARCHAR(50),
    name VARCHAR(20)
);
