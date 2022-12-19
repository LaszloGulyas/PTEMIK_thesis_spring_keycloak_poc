#!/bin/bash
mvn clean install -DskipTests
docker image rm spring-keycloak-poc-springapp
docker-compose -f docker-compose.yml up -d
