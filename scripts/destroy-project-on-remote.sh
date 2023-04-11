#!/bin/bash

# $1 : remote server address
# $2 : remote server user

if [ $# -ne 2 ]; then
    echo "Exactly 2 arguments required: ssh host name , ssh user"
    exit 1
fi

echo "Destroying project on remote server..."
ssh -t "$2"@"$1" -- "sh -c 'cd spring-keycloak-poc/scripts && ./purge-project.sh && cd .. && cd .. && rm -rf spring-keycloak-poc && exit'"

echo "Destroying completed!"
