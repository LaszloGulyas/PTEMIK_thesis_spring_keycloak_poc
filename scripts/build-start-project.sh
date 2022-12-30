#!/bin/bash
cd "../backend/spring-rest-server" || exit
./mvnw clean install -DskipTests
cd "../.."
docker image rm spring-keycloak-poc-springapp
docker-compose -f docker-compose.yml up -d
