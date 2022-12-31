package com.tm7xco.keycloakrestclientlib.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak-rest-client")
@Data
public class KeycloakRestClientConfigProperties {

    private String url;
    private String realm;
    private String clientId;
    private Admin admin;

    @Data
    public static class Admin {
        private String clientId;
        private String username;
        private String password;
    }

}
