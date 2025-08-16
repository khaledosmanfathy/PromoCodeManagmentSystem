# PromoCodeManagmentSystem

A Spring Boot-based REST API for managing promo codes.
 This project uses Java, Spring Boot, Maven, SQL, and Lombok. 
 It provides endpoints to create, update, delete, search, and apply promo codes.

## Features

- Create, update, delete promo codes (Admin only)
- Retrieve promo codes by id, list active and expired codes
- Search promo codes by code, status, and date range (Admin and Business roles)
- Apply a promo code (public endpoint)

## Technologies Used

- Java
- Spring Boot
- Maven
- SQL
- Spring Security
- Jakarta Validation
- Lombok

## Prerequisites

- JDK 11 or higher
- Maven 3.6+
- A SQL database (e.g., MySQL, PostgreSQL)
- Git

## Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/khaledosmanfathy/repo-name.git
   cd repo-name
   ```

# Configure the Database  
Update your database settings in src/main/resources/application.properties (or .yml):  
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Build the Project 
 
Run the following command to build the project:  
mvn clean install
Run the Application  Start the Spring Boot application:  
mvn spring-boot:run
The API will be available at http://localhost:8080/api/promo-codes.  
API Endpoints
POST /api/promo-codes Create a new promo code. Access: ADMIN  
PUT /api/promo-codes/{id} Update an existing promo code. Access: ADMIN  
DELETE /api/promo-codes/{id} Delete a promo code. Access: ADMIN  
GET /api/promo-codes/{id} Retrieve a promo code by id. Access: ADMIN, BUSINESS  
GET /api/promo-codes List all promo codes. Access: ADMIN, BUSINESS  
GET /api/promo-codes/active List active promo codes. Access: ADMIN, BUSINESS  
GET /api/promo-codes/expired List expired promo codes. Access: ADMIN, BUSINESS  
GET /api/promo-codes/search Search promo codes by code, status, and date range. Access: ADMIN, BUSINESS  
POST /api/promo-codes/apply/{code} Apply a promo code (public endpoint)


# Generate KeyCloak Token:

``curl -X POST http://localhost:8081/realms/promo-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=promo-management" \
  -d "client_secret=dev-secret" \
  -d "username=admin" \
  -d "password=admin" \
  -d "grant_type=password"
``

# Testing API Endpoints

1. **Create Promo Code (Admin Only)
``curl -X POST \
  http://localhost:8082/api/promo-codes \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "X-Tenant-ID: tenant1" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "SUMMER25",
    "amount": 25,
    "discountType": "PERCENTAGE",
    "expiryDate": "2025-12-31",
    "usageLimit": 100
  }'``
  
2. **Get All Promo Codes
``curl -X GET \
  http://localhost:8082/api/promo-codes \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "X-Tenant-ID: tenant1"``
  
3. **Apply Promo Code
``curl -X POST \
  http://localhost:8082/api/promo-codes/apply/SUMMER25 \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "X-Tenant-ID: tenant1"``
  
4. **Filter Promo Codes

``curl -X GET \
  "http://localhost:8082/api/promo-codes/search?status=ACTIVE" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "X-Tenant-ID: tenant1"``
  
  
# Testing Role-Based Access

# As business user (should get 403)
``curl -X DELETE \
  http://localhost:8082/api/promo-codes/1 \
  -H "Authorization: Bearer $BUSINESS_TOKEN" \
  -H "X-Tenant-ID: tenant1"``

# As admin user (should succeed)
``curl -X DELETE \
  http://localhost:8082/api/promo-codes/1 \
  -H "Authorization: Bearer $BUSINESS_TOKEN" \
  -H "X-Tenant-ID: tenant1"``