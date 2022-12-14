package com.tm7xco.springkeycloakpoc.keycloak;

import com.tm7xco.springkeycloakpoc.config.KeycloakApiConfig;
import com.tm7xco.springkeycloakpoc.keycloak.dto.KeycloakTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakApi keycloakApi;
    private final KeycloakApiConfig keycloakApiConfig;

    public boolean createKeycloakUser(String username, String password, boolean enabled) {
        String adminBearerToken = getAdminBearerToken();

        ResponseEntity<String> response = null;
        try {
            response = keycloakApi.createUser(username, password, enabled, "SpringKeycloakPoc", adminBearerToken);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return (response != null && response.getStatusCode().is2xxSuccessful()); // user created in keycloak
    }

    private String getAdminBearerToken() {
        ResponseEntity<KeycloakTokenResponse> response = keycloakApi.getBearerTokenByPassword(keycloakApiConfig.getAdminUsername(), keycloakApiConfig.getAdminPassword(), keycloakApiConfig.getAdminClientId());

        if (response == null || response.getBody() == null || response.getBody().getAccessToken() == null) {
            throw new RuntimeException("Error retrieving admin bearer token from Keycloak");
        }

        return response.getBody().getAccessToken();
    }

}
