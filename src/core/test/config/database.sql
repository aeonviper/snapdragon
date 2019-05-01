--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

DROP TABLE IF EXISTS item;

CREATE TABLE item (
    id numeric NOT NULL,
    "name" text,
    "value" text,
    "version" integer DEFAULT 0,
    "created" bigint DEFAULT 0
);

ALTER TABLE ONLY item
    ADD CONSTRAINT item_pkey PRIMARY KEY (id);

ALTER TABLE ONLY item
    ADD CONSTRAINT unique_value_name UNIQUE ("name", "value");
    