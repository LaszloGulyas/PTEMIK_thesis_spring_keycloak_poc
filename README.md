# Spring - Keycloak integration POC

## Introduction
The purpose of this web application is to integrate java spring backend with keycloak within a containerized environment.


## Architecture
- Backend: Java 17 - Spring (REST API)
- Keycloak
- Postgres
- Docker

## Core dependencies

The project contains the following frameworks and key dependencies:
- Java 17
    - Spring-Boot 3.0.0
- Keycloak 19.0.2
- Postgres 15.1

## Environment details
- spring app is running on http://localhost:8080
- keycloak is running on http://localhost:8081/
  - default user/pass: admin/admin
- CORS policy is open for any URL on backend
    - white-listed sources:
        - http://127.0.0.1
    - white-listed HTTP methods: GET, POST, PUT, DELETE

## API documentation
List of open APIs
- GET /actuator/health
- GET /api/user/logout
- POST /api/user/login
- POST /api/user/register
