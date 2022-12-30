#!/bin/bash
cd ".."
docker-compose -f docker-compose.yml down
docker volume rm spring-keycloak-poc_postgres-data
docker volume rm spring-keycloak-poc_springapp-data
