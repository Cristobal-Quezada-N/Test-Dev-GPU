-- ==========
-- Entities
-- ==========

DROP TABLE IF EXISTS item         CASCADE;
DROP TABLE IF EXISTS app_user     CASCADE;
DROP TABLE IF EXISTS auth_factor  CASCADE;
DROP TABLE IF EXISTS loan         CASCADE;

CREATE TABLE item (
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(50) NOT NULL
);

CREATE TABLE app_user (
    id        VARCHAR(50) PRIMARY KEY,
    role_id   INTEGER     NOT NULL REFERENCES role(id)        ON UPDATE RESTRICT ON DELETE RESTRICT,
    status_id INTEGER     NOT NULL REFERENCES user_status(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    email     VARCHAR(50) NOT NULL UNIQUE,
    password  VARCHAR(255)   NOT NULL
);

CREATE TABLE auth_factor (
    id              SERIAL PRIMARY KEY,
    user_id         VARCHAR(50) NOT NULL REFERENCES app_user(id)         ON UPDATE RESTRICT ON DELETE RESTRICT,
    type_id         INTEGER     NOT NULL REFERENCES auth_factor_type(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    used            BOOLEAN     NOT NULL,
    creation_date   DATE        NOT NULL,
    expiration_date DATE        NOT NULL
);

CREATE TABLE loan (
    id        SERIAL PRIMARY KEY,
    user_id   VARCHAR(50) NOT NULL REFERENCES app_user(id)    ON UPDATE RESTRICT ON DELETE RESTRICT,
    item_id   INTEGER     NOT NULL REFERENCES item(id)        ON UPDATE RESTRICT ON DELETE RESTRICT,
    status_id INTEGER     NOT NULL REFERENCES user_status(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    date      DATE        NOT NULL,
    deadline  DATE        NOT NULL
);
