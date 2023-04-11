#!/bin/bash

# $1 : remote server address
# $2 : remote server user

if [ $# -ne 2 ]; then
    echo "Exactly 2 arguments required: ssh host name , ssh user"
    exit 1
fi

echo "Zipping project files..."
cd ..
cd ..
zip -vrq spring-keycloak-poc.zip spring-keycloak-poc

echo "Copying project zip to remote server..."
scp spring-keycloak-poc.zip "$2"@"$1":~/
rm -f spring-keycloak-poc.zip

echo "Deploying project on remote server..."
ssh -t "$2"@"$1" -- "sh -c 'unzip spring-keycloak-poc.zip && rm spring-keycloak-poc.zip && cd spring-keycloak-poc/scripts && export KEYCLOAK_HOSTNAME=\"$1\" && ./build-start-project.sh && exit'"

echo "Deployment completed!"
