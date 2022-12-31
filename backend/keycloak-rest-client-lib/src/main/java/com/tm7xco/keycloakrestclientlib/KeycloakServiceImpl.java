package com.tm7xco.keycloakrestclientlib;

import com.tm7xco.keycloakrestclientlib.api.KeycloakRestClient;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakRealmRoleResponse;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakTokenResponse;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

    private final KeycloakRestClient keycloakRestClient;

    public String getUserBearerToken(String username, String password) {
        String userBearerToken = null;

        try {
            userBearerToken = extractToken(keycloakRestClient.getUserBearerToken(username, password));
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }

        return userBearerToken;
    }

    public String getUsernameById(String userId) {
        ResponseEntity<KeycloakUserResponse> response = null;

        try {
            response = keycloakRestClient.getUserById(userId, getAdminBearerToken());
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }

        String username = null;
        if (response != null && response.getBody() != null) {
            username = response.getBody().getUsername();
        }

        return username;
    }

    public boolean createKeycloakUser(String username, String password, boolean enabled) {
        ResponseEntity<String> response = null;

        try {
            response = keycloakRestClient.createUser(username, password, enabled, getAdminBearerToken());
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }

        return (response != null && response.getStatusCode().is2xxSuccessful());
    }

    public boolean deleteKeycloakUser(String userId) {
        ResponseEntity<String> response = null;

        try {
            response = keycloakRestClient.deleteUser(userId, getAdminBearerToken());
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }

        return (response != null && response.getStatusCode().is2xxSuccessful());
    }

    public boolean updateUserPassword(String userId, String newPassword) {
        ResponseEntity<String> response = null;

        try {
            response = keycloakRestClient.updatePassword(userId, newPassword, getAdminBearerToken());
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }

        return (response != null && response.getStatusCode().is2xxSuccessful());
    }

    public boolean addRoleToUser(String userId, String roleName) {
        return updateRoleOfUser(userId, roleName, true);
    }

    public boolean revokeRoleFromUser(String userId, String role) {
        return updateRoleOfUser(userId, role, false);
    }

    private boolean updateRoleOfUser(String userId, String roleName, boolean assignment) {
        ResponseEntity<String> response = null;

        try {
            if (assignment) {
                response = keycloakRestClient.addRealmRoleToUser(userId, getRealmRoleId(roleName), roleName, getAdminBearerToken());
            } else {
                response = keycloakRestClient.deleteRealmRoleFromUser(userId, getRealmRoleId(roleName), roleName, getAdminBearerToken());
            }
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }

        return (response != null && response.getStatusCode().is2xxSuccessful());
    }

    private String getAdminBearerToken() {
        return extractToken(keycloakRestClient.getAdminBearerToken());
    }

    private String extractToken(ResponseEntity<KeycloakTokenResponse> response) {
        if (response == null || response.getBody() == null || response.getBody().getAccessToken() == null) {
            throw new RuntimeException("Error transforming bearer token from Keycloak response");
        }
        return response.getBody().getAccessToken();
    }

    private String getRealmRoleId(String roleName) {
        ResponseEntity<KeycloakRealmRoleResponse> response = keycloakRestClient.getRealmRoleByName(roleName, getAdminBearerToken());
        return Objects.requireNonNull(response.getBody()).getId();
    }

}
