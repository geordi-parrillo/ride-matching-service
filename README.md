# Ride Matching Service

Spring Boot service for managing drivers and rides, with nearest-driver matching and in-memory persistence.

## Prerequisites

Before starting the project, make sure you have:

- Java 25
- Maven wrapper available in the repository (`./mvnw`)
- Git

If the wrapper is not executable, run:

```bash
chmod +x mvnw
```

## Setup

1. Clone the repository:

```bash
git clone <repository-url>
cd ride-matching-service
```

2. Validate the environment and dependencies:

```bash
./mvnw test
```

This downloads the required dependencies and verifies the project compiles and the tests pass.

## Run the application

Start the Spring Boot application with:

```bash
./mvnw spring-boot:run
```

The service listens on:

```text
http://localhost:8080
```

## OpenAPI / Swagger UI

The project includes Springdoc OpenAPI support, so once the application is running you can explore the generated API documentation at:

```text
http://localhost:8080/swagger-ui.html
```

The OpenAPI JSON document is available at:

```text
http://localhost:8080/v3/api-docs
```

## Useful commands

Run all tests:

```bash
./mvnw test
```

Clean and rebuild:

```bash
./mvnw clean package
```

Run the app in development mode:

```bash
./mvnw spring-boot:run
```

## API overview

The project exposes REST endpoints under the `/api` path.

### Driver endpoints

- `POST /api/drivers`
  - Create a driver
  - Body example:

```json
{
  "x": 10.5,
  "y": 20.2,
  "available": true
}
```

- `PUT /api/drivers/{id}`
  - Update a driver

- `GET /api/drivers/available?x=0&y=0&limit=10`
  - Return the nearest available drivers from the given location

### Ride endpoints

- `POST /api/rides`
  - Create a ride request
  - Body example:

```json
{
  "riderId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "x": 12.0,
  "y": 14.0
}
```

- `POST /api/rides/{id}/complete`
  - Mark a ride as completed

## Notes

- The project uses in-memory repositories, so data is reset when the application restarts.
- The service is designed around a simple nearest-driver matching flow.
