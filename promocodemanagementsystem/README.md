# Promo Code Management System

## Description

A multi-tenant Promo Code Management System with both backend and frontend components. The system allows different tenants (clients) to manage promo codes, track their usage, and view reports while maintaining complete tenant data isolation.

## Features

- **Promo Code Configuration:** Create, update, and delete promo codes.
- **Reporting:** List all promo codes for the tenant, show usage count per promo code, and filter by date range, status, and code.
- **Multi-tenancy:** Schema-per-tenant approach with PostgreSQL.
- **Authentication & Authorization:** All APIs are secured with Keycloak, with different roles for Admin and Business users.

## Technologies

- **Backend:** Java 21, Spring Boot 3.5+, PostgreSQL, Keycloak
- **Frontend:** Angular (LTS)

## Setup

### Prerequisites

- Java 21
- Maven
- PostgreSQL
- Keycloak
- Docker

### Backend

1.  **Clone the repository:**

    ```bash
    git clone <repository-url>
    ```

2.  **Configure the database:**

    - Create a PostgreSQL database named `promo_system`.
    - Update the `application.yml` file with your database credentials.

3.  **Configure Keycloak:**

    - Start a Keycloak instance.
    - Create a new realm named `promo-realm`.
    - Create a new client named `promo-management`.
    - Configure the client with the correct access type and valid redirect URIs.
    - Create two roles: `ADMIN` and `BUSINESS`.
    - Create users and assign them to the appropriate roles.
    - Update the `application.yml` file with your Keycloak configuration.

4.  **Run the application:**

    ```bash
    mvn spring-boot:run
    ```

### Frontend

(Instructions to be added once the frontend is implemented)

## API Endpoints

| Method | Path                  | Description                   | Roles          |
| ------ | --------------------- | ----------------------------- | -------------- |
| POST   | /api/promo-codes      | Create a new promo code       | ADMIN          |
| PUT    | /api/promo-codes/{id} | Update an existing promo code | ADMIN          |
| DELETE | /api/promo-codes/{id} | Delete a promo code           | ADMIN          |
| GET    | /api/promo-codes/{id} | Get a promo code by ID        | ADMIN, BUSINESS|
| GET    | /api/promo-codes      | Get all promo codes           | ADMIN, BUSINESS|
| GET    | /api/promo-codes/active | Get all active promo codes    | ADMIN, BUSINESS|
| GET    | /api/promo-codes/expired| Get all expired promo codes   | ADMIN, BUSINESS|
| GET    | /api/promo-codes/search | Search for promo codes        | ADMIN, BUSINESS|
| POST   | /api/promo-codes/apply/{code} | Apply a promo code | (any)          |

## Database Schema

| Column       | Type          | Constraints               | Description                               |
| ------------ | ------------- | ------------------------- | ----------------------------------------- |
| id           | BIGINT        | PRIMARY KEY, AUTO_INCREMENT | The unique identifier for the promo code. |
| code         | VARCHAR(50)   | NOT NULL, UNIQUE          | The promo code string.                    |
| amount       | DOUBLE        | NOT NULL                  | The discount amount.                      |
| discount_type| VARCHAR(255)  | NOT NULL                  | The type of discount (e.g., FIXED, PERCENTAGE). |
| expiry_date  | TIMESTAMP     | NOT NULL                  | The expiry date of the promo code.        |
| usage_limit  | INTEGER       |                           | The maximum number of times the promo code can be used. |
| usage_count  | INTEGER       | DEFAULT 0                 | The number of times the promo code has been used. |
| status       | VARCHAR(255)  | NOT NULL, DEFAULT 'ACTIVE'| The status of the promo code (e.g., ACTIVE, EXPIRED, DISABLED). |
| created_at   | TIMESTAMP     |                           | The date and time when the promo code was created. |
| updated_at   | TIMESTAMP     |                           | The date and time when the promo code was last updated. |

## Multi-tenancy

The application uses a schema-per-tenant approach. The tenant is resolved from the JWT token provided by Keycloak. The `TenantIdentifierResolver` extracts the tenant ID from the `tenant_id` claim in the JWT token. The `TenantConnectionProvider` then sets the schema for the current tenant using the `SET SCHEMA` command.

## Authentication and Authorization

The application uses Keycloak for authentication and authorization. All APIs are secured using OAuth2 and JWT. The `@PreAuthorize` annotation is used to enforce role-based access control.

## Docker

### Build the Docker image

```bash
docker build -t promo-management .
```

### Run the Docker image

```bash
docker run -p 8082:8082 -e KEYCLOAK_CLIENT_SECRET=<your-client-secret> -e DEFAULT_TENANT=<your-default-tenant> promo-management
```
