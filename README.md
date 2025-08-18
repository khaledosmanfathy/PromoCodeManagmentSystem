# Promo Code Management System

## Objective

This project is a multi-tenant Promo Code Management System with both backend and frontend components. The system allows different tenants (clients) to manage promo codes, track their usage, and view reports while maintaining complete tenant data isolation.

## Key Requirements

### 1. Core Features

-   **Promo Code Configuration:** Create, update, and delete promo codes.
    -   **Code:** (string)
    -   **Amount:** (discount value, percentage or fixed)
    -   **Expiry Date**
    -   **Usage Limit:** (optional)
    -   **Status:** (Active / Expired / Disabled)
-   **Reporting:**
    -   List all promo codes for the tenant.
    -   Show usage count per promo code.
    -   Ability to filter by date range, status, and code.

### 2. Multi-Tenancy

-   Implemented a schema-per-tenant approach using PostgreSQL.
-   All API calls are tenant-aware to isolate each tenant's data.
-   Tenant information is derived from Keycloak authentication.

### 3. Authentication & Authorization

-   All APIs are secured.
-   Different roles are supported (Admin: Full access & Business: Read Only).

### 4. Backend (`promocodemanagementsystem`)

-   Java 21 & Spring Boot 3.5+
-   RESTful APIs for:
    -   Promo code CRUD operations.
    -   Reporting.
-   Tenant context resolution for all backend operations.
-   Centralized exception handling.
-   API-level validation.

### 5. Frontend (`promo-code-management-ui`)

-   Angular (LTS)
-   UI for:
    -   Login
    -   Promo code management form.
    -   Reporting table with filters.
-   Securely calls backend APIs.

## Deliverables

-   **Source Code:**
    -   Backend code (Spring Boot) in `promocodemanagementsystem/`
    -   Frontend code (Angular) in `promo-code-management-ui/`
-   **Dockerfiles:** For both frontend and backend.
-   **README:** This file with setup instructions.

## Setup and Running the Application

### Prerequisites

-   Docker and Docker Compose
-   Java 21+
-   Maven 3.8+
-   Node.js and npm (for local frontend development)

### Using Docker Compose (Recommended)

This is the easiest way to get the entire system running.

1.  **Build the applications:**
    -   **Backend:**
        ```bash
        cd promocodemanagementsystem
        mvn clean package
        ```
    -   **Frontend:**
        ```bash
        cd promo-code-management-ui
        npm install
        npm run build
        ```

2.  **Run with Docker Compose:**
    From the root directory, run:
    ```bash
    docker-compose up --build
    ```

    This will start the following services:
    -   `postgres-db`: PostgreSQL database.
    -   `keycloak`: Keycloak authentication server.
    -   `promo-code-backend`: The Spring Boot backend application.
    -   `promo-code-frontend`: The Angular frontend application.

### Accessing the Applications

-   **Frontend:** `http://localhost:4200`
-   **Backend API:** `http://localhost:8082`
-   **Keycloak Admin Console:** `http://localhost:8081`
    -   **Admin credentials:** `admin`/`admin`

### Local Development (without Docker)

#### Backend

1.  Navigate to the `promocodemanagementsystem` directory.
2.  Run `mvn spring-boot:run`. The application will be available at `http://localhost:8082`.

#### Frontend

1.  Navigate to the `promo-code-management-ui` directory.
2.  Run `npm install`.
3.  Run `ng serve`. The application will be available at `http://localhost:4200`.