package com.tm7xco.keycloakrestclientlib.autoconfigure;

import com.tm7xco.keycloakrestclientlib.api.KeycloakRestClient;
import com.tm7xco.keycloakrestclientlib.api.KeycloakRestClientImpl1902;
import com.tm7xco.keycloakrestclientlib.KeycloakService;
import com.tm7xco.keycloakrestclientlib.KeycloakServiceImpl;
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
        return new KeycloakServiceImpl(createKeycloakApi());
    }

    private KeycloakRestClient createKeycloakApi() {
        return new KeycloakRestClientImpl1902(createRestTemplate(), config);
    }

    private RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

}
