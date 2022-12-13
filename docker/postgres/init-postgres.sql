CREATE USER springapp;
CREATE DATABASE springapp;
GRANT ALL PRIVILEGES ON DATABASE springapp TO springapp;
ALTER USER springapp with PASSWORD 'springapp';

CREATE USER keycloak;
CREATE DATABASE keycloak;
GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;
ALTER USER keycloak with PASSWORD 'keycloak';
