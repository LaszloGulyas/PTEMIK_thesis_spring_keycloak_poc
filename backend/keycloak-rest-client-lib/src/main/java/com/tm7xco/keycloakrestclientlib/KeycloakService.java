package com.tm7xco.keycloakrestclientlib;

import com.tm7xco.keycloakrestclientlib.autoconfigure.KeycloakRestClientConfigProperties;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakRealmRoleResponse;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakTokenResponse;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakApi keycloakApi;
    private final KeycloakRestClientConfigProperties keycloakApiConfig;

    public String getUserBearerToken(String username, String password) {
        String userBearerToken = null;

        try {
            String url = keycloakApiConfig.getUrl() + "/realms/" + keycloakApiConfig.getUser().getRealm() + "/protocol/openid-connect/token";
            userBearerToken = getBearerTokenByPassword(
                    url,
                    username,
                    password,
                    keycloakApiConfig.getUser().getClientId()
            );
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }

        return userBearerToken;
    }

    public String getUsernameById(String userId) {
        ResponseEntity<KeycloakUserResponse> response = null;

        try {
            response = keycloakApi.getUserById(
                    userId,
                    keycloakApiConfig.getUser().getRealm(),
                    getAdminBearerToken());
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
            response = keycloakApi.createUser(username, password, enabled,
                    keycloakApiConfig.getUser().getRealm(), getAdminBearerToken());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return (response != null && response.getStatusCode().is2xxSuccessful());
    }

    public boolean deleteKeycloakUser(String userId) {
        ResponseEntity<String> response = null;

        try {
            response = keycloakApi.deleteUser(userId, keycloakApiConfig.getUser().getRealm(), getAdminBearerToken());
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
        }

        return (response != null && response.getStatusCode().is2xxSuccessful());
    }

    public boolean updateUserPassword(String userId, String newPassword) {
        ResponseEntity<String> response = null;

        try {
            response = keycloakApi.updatePassword(userId, newPassword, keycloakApiConfig.getUser().getRealm(), getAdminBearerToken());
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
                response = keycloakApi.addRealmRoleToUser(userId, getRealmRoleId(roleName), roleName,
                        keycloakApiConfig.getUser().getRealm(), getAdminBearerToken());
            } else {
                response = keycloakApi.deleteRealmRoleFromUser(userId, getRealmRoleId(roleName), roleName,
                        keycloakApiConfig.getUser().getRealm(), getAdminBearerToken());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return (response != null && response.getStatusCode().is2xxSuccessful());
    }

    private String getBearerTokenByPassword(String url, String username, String password, String clientId) {
        ResponseEntity<KeycloakTokenResponse> response = keycloakApi.getBearerTokenByPassword(
                url, username, password, clientId);

        if (response == null || response.getBody() == null || response.getBody().getAccessToken() == null) {
            throw new RuntimeException("Error retrieving admin bearer token from Keycloak");
        }

        return response.getBody().getAccessToken();
    }

    private String getAdminBearerToken() {
        String url = keycloakApiConfig.getUrl() + "/realms/master/protocol/openid-connect/token";
        return getBearerTokenByPassword(
                url,
                keycloakApiConfig.getAdmin().getUsername(),
                keycloakApiConfig.getAdmin().getPassword(),
                keycloakApiConfig.getAdmin().getClientId());
    }

    private String getRealmRoleId(String roleName) {
        ResponseEntity<KeycloakRealmRoleResponse> response = keycloakApi.getRealmRoleByName(
                roleName,
                keycloakApiConfig.getUser().getRealm(),
                getAdminBearerToken());
        return Objects.requireNonNull(response.getBody()).getId();
    }

}
