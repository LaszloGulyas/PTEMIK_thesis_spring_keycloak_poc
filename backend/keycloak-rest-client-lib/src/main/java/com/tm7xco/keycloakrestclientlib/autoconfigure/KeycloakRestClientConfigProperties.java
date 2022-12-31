package com.tm7xco.keycloakrestclientlib.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak-rest-client")
@Data
public class KeycloakRestClientConfigProperties {

    private String url;
    private Admin admin;
    private User user;

    @Data
    public static class Admin {
        private String clientId;
        private String username;
        private String password;
    }

    @Data
    public static class User {
        private String clientId;
        private String realm;
    }

}
