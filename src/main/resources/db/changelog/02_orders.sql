--liquibase formatted sql

--changeset Stik:orders
CREATE TABLE IF NOT EXISTS orders(
    "id"            UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    "movie_id"      UUID        REFERENCES movie("id") ,
    "order_time"    Timestamp   NOT NULL DEFAULT(now()),
    "participants"  INT         NOT NULL,
    CHECK (participants> 0)
);

