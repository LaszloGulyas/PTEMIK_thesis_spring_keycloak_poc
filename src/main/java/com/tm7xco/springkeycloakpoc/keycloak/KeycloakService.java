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

    public String getUserBearerToken(String username, String password) {
        String userBearerToken = null;

        try {
            String url = keycloakApiConfig.getUrl() + "/realms/" + keycloakApiConfig.getUserRealm() + "/protocol/openid-connect/token";
            userBearerToken = getBearerTokenByPassword(
                    url,
                    username,
                    password,
                    keycloakApiConfig.getUserClientId()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return userBearerToken;
    }

    public boolean createKeycloakUser(String username, String password, boolean enabled) {
        ResponseEntity<String> response = null;

        try {
            String url = keycloakApiConfig.getUrl() + "/realms/master/protocol/openid-connect/token";
            String adminBearerToken = getBearerTokenByPassword(
                    url,
                    keycloakApiConfig.getAdminUsername(),
                    keycloakApiConfig.getAdminPassword(),
                    keycloakApiConfig.getAdminClientId()
            );

            response = keycloakApi.createUser(username, password, enabled, "SpringKeycloakPoc", adminBearerToken);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return (response != null && response.getStatusCode().is2xxSuccessful()); // user created in keycloak
    }

    private String getBearerTokenByPassword(String url, String username, String password, String clientId) {
        ResponseEntity<KeycloakTokenResponse> response = keycloakApi.getBearerTokenByPassword(
                url, username, password, clientId);

        if (response == null || response.getBody() == null || response.getBody().getAccessToken() == null) {
            throw new RuntimeException("Error retrieving admin bearer token from Keycloak");
        }

        return response.getBody().getAccessToken();
    }

}
