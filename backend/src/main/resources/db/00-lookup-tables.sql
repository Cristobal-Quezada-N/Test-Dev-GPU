-- ==========
-- Lookup Tables
-- ==========

DROP TABLE IF EXISTS user_status              CASCADE;
DROP TABLE IF EXISTS role                     CASCADE;
DROP TABLE IF EXISTS loan_status              CASCADE;
DROP TABLE IF EXISTS auth_factor_type         CASCADE;
DROP TABLE IF EXISTS activation_token_status  CASCADE;
DROP TABLE IF EXISTS item                     CASCADE;

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

CREATE TABLE activation_token_status (
    id    SERIAL PRIMARY KEY,
    code  VARCHAR(20) NOT NULL UNIQUE,
    name  VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE item (
    id                SERIAL PRIMARY KEY,
    name              VARCHAR(100)  NOT NULL,
    description       TEXT,
    category          VARCHAR(50)   NOT NULL DEFAULT 'MISC',
    min_people        INTEGER       NOT NULL DEFAULT 1,
    max_usage_minutes INTEGER       NOT NULL DEFAULT 80,
    image_url         VARCHAR(255)  NOT NULL DEFAULT 'default-item.webp',
    created_at        TIMESTAMP     WITH TIME ZONE NOT NULL DEFAULT NOW()
);
