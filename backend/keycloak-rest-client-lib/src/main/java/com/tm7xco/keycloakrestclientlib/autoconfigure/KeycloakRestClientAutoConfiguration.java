package com.tm7xco.keycloakrestclientlib.autoconfigure;

import com.tm7xco.keycloakrestclientlib.KeycloakApi;
import com.tm7xco.keycloakrestclientlib.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@AutoConfiguration
@RequiredArgsConstructor
public class KeycloakRestClientAutoConfiguration {

    private final KeycloakRestClientConfigProperties config;

    @Bean
    public KeycloakService createKeycloakService() {
        return new KeycloakService(createKeycloakApi(), config);
    }

    private KeycloakApi createKeycloakApi() {
        return new KeycloakApi(createRestTemplate(), config);
    }

    private RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

}
