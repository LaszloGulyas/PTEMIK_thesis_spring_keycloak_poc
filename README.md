# Spring - Keycloak integration POC

## Introduction
The purpose of this web application is to integrate java spring backend with keycloak within a containerized environment.


## Architecture
- Backend: Java 17 - Spring (REST API)
- Keycloak
- Postgres
- Apps run in dockerized containers

## Core dependencies

The project contains the following frameworks and key dependencies:
- Java 17
    - Spring-Boot 3.0.0
      - Spring-Security 6.0.0
      - OAuth2 Resource Server 6.0.0
- Keycloak 19.0.2
- Postgres 14.0

## Environment details
- spring app is running on http://localhost:8080
- keycloak is running on http://localhost:8081/
  - default user/pass: admin/admin
- CORS policy is open for any URL on backend
    - white-listed sources:
        - http://127.0.0.1
    - white-listed HTTP methods: GET, POST, PUT, DELETE

## API documentation
List of API endpoints:
- open:
  - GET /actuator/health
  - POST /api/user/login
  - POST /api/user/register
- secured:
  - GET /api/business/execute
  - PUT /api/user/update-password
  - DELETE /api/user

## How to build / start the project
- Start project in fully dockerized environment:
  - ```cd``` to the root project folder
  - run ```./scripts/build-start-project.sh``` to
    - clean install project with maven
    - remove existing springapp image from docker
    - build springapp docker image
    - start docker containers
  - run ```./scripts/stop-project.sh``` to
    - stop and remove docker containers
    - keep docker images
    - keep docker volumes
  - run ```./scripts/purge-project.sh``` to
    - stop and remove docker containers
    - keep docker images
    - remove docker volumes
- Start project without a containerized springapp to allow local run and debugging:
  - run ```./scripts/local-start-containers.sh``` to
    - start postgres / keycloak containers
    - then run springapp jar manually or from IDE
  - run ```./scripts/local-stop-containers.sh``` to
    - stop and remove docker containers
    - keep docker images
    - keep docker volumes
