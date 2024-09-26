# Loan Service

## Overview

Loan Service is part of the Banking Loan Management System, designed to manage customer loans, including loan applications, approval processes, tracking loan schedules, and handling various loan types. This microservice handles the full lifecycle of a loan application, from submission and review to approval and document management.

## Features

- **Loan Application Management**: Submit, review, approve, and reject loan applications.
- **Document Handling**: Upload and manage documents related to loan applications.
- **Collateral Management**: Manage collateral for loan applications requiring security.
- **Loan Status Tracking**: Monitor the status of loan applications at various stages (Pending, Reviewing, Approved, Rejected, etc.).
- **Loan Types and Interest Rates**: Manage different loan types and their interest rates.
- **Handling Late Payments**: Process late payments and calculate penalties if applicable.

## Tech Stack

- **Java 21**
- **Spring Boot**
- **Spring Cloud Eureka** (Service Discovery)
- **Spring Cloud OpenFeign** (Service-to-Service Communication)
- **Spring Data JPA** (Data Persistence)
- **MapStruct** (Object Mapping)
- **MySQL 9** (Database)
- **Lombok** (Boilerplate Code Reduction)
- **Swagger/OpenAPI** (API Documentation)

## Getting Started

### Prerequisites

Ensure you have the following tools installed:

- **Java 21**
- **Gradle** (Build System)
- **MySQL 9** (Database)
- **Kafka** (Optional, if using messaging)
- **Eureka Server** (Service Discovery)

### Configuration

1. Clone the repository:

   ```bash
   git clone https://git-internal.kienlongbank.co/tantd1/microservices-core-banking.git
   cd loan-service
   ```

2. Update the `application.yml` file to configure database, Eureka, Kafka, etc.

   Example database configuration:

   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/loan_service_db
       username: your_db_username
       password: your_db_password
     jpa:
       hibernate:
         ddl-auto: update
       show-sql: true
   ```

### Running Docker Compose

Before running the Loan Service, you need to set up the infrastructure, including MySQL and other services, using Docker Compose.

1. Navigate to the root directory of the Banking Loan Service repository:

   ```bash
   cd banking-loan-service
   ```

2. Run the Docker Compose file to start all necessary services (MySQL, Kafka, Eureka, etc.):

   ```bash
   docker-compose -f docker-compose.yml up -d
   ```

   This will start the MySQL database and other services required by Loan Service. Ensure that these services are up and running before proceeding.

3. Verify that MySQL is running by connecting to the container:

   ```bash
   docker exec -it mysql-container-name mysql -u root -p
   ```

### Building and Running the Application

4. Build the project using Gradle:

   ```bash
   ./gradlew build
   ```

5. Run the application:

   ```bash
   ./gradlew bootRun
   ```

### Database Setup

After running Docker Compose and ensuring MySQL is up, you need to set up the database schema.

Run the SQL scripts to set up the required tables in your MySQL database:

- Schema setup script: `banking-loan-service/src/main/resources/db/schema.sql`
- Sample data script: `banking-loan-service/src/main/resources/db/data.sql`

You can use a MySQL client or execute the SQL directly in the running MySQL container:

```bash
docker exec -i mysql-container-name mysql -u your_db_username -p loan_service_db < banking-loan-service/src/main/resources/db/schema.sql
docker exec -i mysql-container-name mysql -u your_db_username -p loan_service_db < banking-loan-service/src/main/resources/db/data.sql
```

## API Endpoints

The service exposes several REST endpoints for loan management. Below are some key endpoints:

- **POST /loan-applications**: Create a new loan application.
- **GET /loan-applications/{id}**: Get loan application details by ID.
- **POST /loan-applications/{id}/approve**: Approve a loan application.
- **POST /loan-applications/{id}/reject**: Reject a loan application.
- **POST /loan-applications/{id}/upload-document**: Upload documents for a loan application.

For detailed API documentation, you can access the Swagger UI when the service is running at:

```
http://localhost:8080/api-docs/swagger-ui
```

## Development

### Code Structure

- **`controller/`**: Contains REST API controllers.
- **`dto/`**: Data Transfer Objects used for API requests and responses.
- **`entity/`**: JPA entities representing the database models.
- **`repository/`**: Repositories for accessing database tables.
- **`service/`**: Business logic implementation.
- **`mapper/`**: Mappers for converting between entities and DTOs.

### Running Tests

You can run unit tests and integration tests using the following command:

```bash
./gradlew test
```

## Deployment