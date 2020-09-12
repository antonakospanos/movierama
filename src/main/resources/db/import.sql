-- User
CREATE USER movierama LOGIN PASSWORD 'movierama' NOCREATEDB NOCREATEROLE NOREPLICATION VALID UNTIL 'infinity';

-- Database
CREATE DATABASE movierama;
GRANT ALL PRIVILEGES ON DATABASE movierama TO movierama;

-- Schema
-- \connect movierama;
CREATE SCHEMA movierama AUTHORIZATION movierama;
SET search_path TO movierama;
