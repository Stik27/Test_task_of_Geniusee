--liquibase formatted sql

--changeset Stik:movie
CREATE TABLE IF NOT EXISTS movie(
    "id"               UUID PRIMARY KEY   DEFAULT gen_random_uuid(),
    "name"             VARCHAR(255)       NOT NULL UNIQUE,
    "release_date"     Timestamp          NOT NULL ,
    "cost"             INT                NOT NULL DEFAULT 0
);
