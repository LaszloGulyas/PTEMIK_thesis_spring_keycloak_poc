#!/bin/bash
cd "../backend/keycloak-rest-client-lib" || exit
./mvnw clean install -DskipTests
cd "../spring-rest-server" || exit
./mvnw clean install -DskipTests
cd "../../frontend/angular-nginx" || exit
npm install
npm run build --prod
cd "../.."
docker image rm spring-keycloak-poc-springapp
docker image rm spring-keycloak-poc-angularapp
docker compose -f docker-compose.yml up -d
