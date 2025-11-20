-- ==========
-- Entities
-- ==========

DROP TABLE IF EXISTS app_user         CASCADE;
DROP TABLE IF EXISTS auth_factor      CASCADE;
DROP TABLE IF EXISTS item_copy        CASCADE;
DROP TABLE IF EXISTS loan             CASCADE;
DROP TABLE IF EXISTS activation_token CASCADE;

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
    creation_date   TIMESTAMP   WITH TIME ZONE NOT NULL,
    expiration_date TIMESTAMP   WITH TIME ZONE NOT NULL
);

CREATE TABLE item_copy (
    id                SERIAL PRIMARY KEY,
    item_id           INTEGER     NOT NULL REFERENCES item(id) ON UPDATE CASCADE ON DELETE RESTRICT,
    copy_number       VARCHAR(20) NOT NULL UNIQUE,
    condition         VARCHAR(20) NOT NULL,
    status            VARCHAR(20) NOT NULL,
    acquisition_date  DATE        NOT NULL,
    notes             TEXT,
    created_at        TIMESTAMP   WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE loan (
    id                SERIAL PRIMARY KEY,
    user_id           VARCHAR(50) NOT NULL REFERENCES app_user(id)    ON UPDATE RESTRICT ON DELETE RESTRICT,
    item_id           INTEGER     NOT NULL REFERENCES item(id)        ON UPDATE RESTRICT ON DELETE RESTRICT,
    status_id         INTEGER     NOT NULL REFERENCES user_status(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    creation_date     TIMESTAMP   WITH TIME ZONE NOT NULL,
    expiration_date   TIMESTAMP   WITH TIME ZONE NOT NULL
);

CREATE TABLE activation_token (
    id                SERIAL PRIMARY KEY,
    user_id           VARCHAR(50) NOT NULL REFERENCES app_user(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    token             VARCHAR(64) NOT NULL UNIQUE,
    status_id         INTEGER     NOT NULL REFERENCES activation_token_status(id) ON UPDATE RESTRICT ON DELETE RESTRICT,
    used_at           TIMESTAMP   WITH TIME ZONE,
    creation_date     TIMESTAMP   WITH TIME ZONE NOT NULL,
    expiration_date   TIMESTAMP   WITH TIME ZONE NOT NULL
);
