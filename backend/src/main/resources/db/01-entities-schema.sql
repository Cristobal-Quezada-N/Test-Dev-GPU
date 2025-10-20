-- ==========
-- Limpieza (debug)
-- ==========

DROP TABLE IF EXISTS Item           CASCADE;
DROP TABLE IF EXISTS App_user       CASCADE;
DROP TABLE IF EXISTS Auth_factor    CASCADE;
DROP TABLE IF EXISTS Loan           CASCADE;

-- ==========
-- Tablas
-- ==========

CREATE TABLE Item (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE App_user (
    id                      VARCHAR(50) PRIMARY KEY,
    role_id                 INTEGER     NOT NULL REFERENCES Role(id)        ON UPDATE RESTRICT ON DELETE RESTRICT,
    status_id               INTEGER     NOT NULL REFERENCES User_status(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    email                   VARCHAR(50) NOT NULL UNIQUE,
    password                VARCHAR(255)   NOT NULL
);

CREATE TABLE Auth_factor (
    id                      SERIAL PRIMARY KEY,
    user_id                 VARCHAR(50) NOT NULL REFERENCES App_user(id)    ON UPDATE RESTRICT ON DELETE RESTRICT,
    type_id                 INTEGER NOT NULL  REFERENCES auth_factor_type(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    used                    BOOLEAN NOT NULL,
    creation_date           DATE    NOT NULL,
    expiration_date         DATE    NOT NULL
);

CREATE TABLE Loan (
    id                      SERIAL PRIMARY KEY,
    user_id                 VARCHAR(50) NOT NULL REFERENCES App_user(id)    ON UPDATE RESTRICT ON DELETE RESTRICT,
    item_id                 INTEGER     NOT NULL REFERENCES Item(id)        ON UPDATE RESTRICT ON DELETE RESTRICT,
    status_id               INTEGER     NOT NULL REFERENCES User_status(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    date                    DATE        NOT NULL,
    deadline                DATE        NOT NULL
);
