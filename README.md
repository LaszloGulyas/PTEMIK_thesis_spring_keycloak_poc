# Spring - Keycloak integration POC

## Introduction
The purpose of this web application is to integrate java spring backend with keycloak within a containerized environment.


## Architecture
- Backend: Java 17 - Spring (REST API)
  - spring-rest-server module
  - keycloak-rest-client-lib
- Frontend: Angular 14 (with nginx)
- Keycloak
- Postgres
- Apps run in dockerized containers

## Core dependencies

The project contains the following frameworks and key dependencies:
- Java 17
    - Spring-Boot 3.0.0
      - Spring-Security 6.0.0
      - OAuth2 Resource Server 6.0.0
- Angular 14.2.12
- Keycloak 19.0.2
- Postgres 14.0

## Environment details
- spring app is running on http://localhost:8080
- keycloak is running on http://localhost:8081/
  - default user/pass: admin/admin
- angular/nginx is listening to http://localhost:4200
- CORS policy is open for any URL on backend
    - white-listed sources:
        - http://localhost:4200
    - white-listed HTTP methods: GET, POST, PUT, DELETE

## API documentation
List of API endpoints:
- open:
  - GET /actuator/health
  - POST /api/user/login
  - POST /api/user/register
- secured:
  - GET /api/business/user/execute (role: user, superuser, admin)
  - GET /api/business/super-user/execute (role: superuser, admin)
  - GET /api/business/admin/execute (role: admin)
  - PUT /api/user/update-password
  - DELETE /api/user

## How to build / start the project
- Start project in fully dockerized environment:
  - ```cd``` to the ```/scripts``` folder
  - run ```./build-start-project.sh``` to
    - clean install keycloak-rest-client-lib with maven
    - clean install spring-rest-server with maven
    - remove existing springapp image from docker
    - build springapp docker image
    - start docker containers
  - run ```./stop-project.sh``` to
    - stop and remove docker containers
    - keep docker images
    - keep docker volumes
  - run ```./purge-project.sh``` to
    - stop and remove docker containers
    - keep docker images
    - remove docker volumes
- Start project without a containerized springapp to allow local run and debugging:
  - run ```./local-start-containers.sh``` to
    - start postgres / keycloak containers
    - then run springapp jar manually or from IDE
  - run ```./local-stop-containers.sh``` to
    - stop and remove docker containers
    - keep docker images
    - keep docker volumes
