package com.tm7xco.springkeycloakpoc.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KeycloakApiConfig {

    @Value("${spring-keycloak-poc.keycloak.url}")
    private String url;

    @Value("${spring-keycloak-poc.keycloak.admin.username}")
    private String adminUsername;

    @Value("${spring-keycloak-poc.keycloak.admin.password}")
    private String adminPassword;

    @Value("${spring-keycloak-poc.keycloak.admin.client-id}")
    private String adminClientId;

    @Value("${spring-keycloak-poc.keycloak.user.client-id}")
    private String userClientId;

    @Value("${spring-keycloak-poc.keycloak.user.realm}")
    private String userRealm;

}
