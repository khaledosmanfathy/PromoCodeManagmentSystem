-- Create Keycloak database
CREATE USER keycloak WITH PASSWORD 'keycloak';
CREATE DATABASE keycloak_db WITH OWNER keycloak;
GRANT ALL PRIVILEGES ON DATABASE keycloak_db TO keycloak;

-- Create application schemas
\c promo_system

CREATE SCHEMA tenant1;
CREATE SCHEMA tenant2;

-- Create tables in all schemas
CREATE TABLE tenant1.promo_codes (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    amount NUMERIC NOT NULL,
    discount_type VARCHAR(20) NOT NULL CHECK (discount_type IN ('FIXED', 'PERCENTAGE')),
    expiry_date TIMESTAMP NOT NULL,
    usage_limit INTEGER,
    usage_count INTEGER DEFAULT 0,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'EXPIRED', 'DISABLED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create identical structure for tenant2
CREATE TABLE tenant2.promo_codes (LIKE tenant1.promo_codes INCLUDING ALL);

-- Set permissions
GRANT ALL PRIVILEGES ON SCHEMA tenant1 TO postgres;
GRANT ALL PRIVILEGES ON SCHEMA tenant2 TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA tenant1 TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA tenant2 TO postgres;