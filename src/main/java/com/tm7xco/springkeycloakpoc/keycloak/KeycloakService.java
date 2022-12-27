package com.tm7xco.springkeycloakpoc.keycloak;

import com.tm7xco.springkeycloakpoc.config.KeycloakApiConfig;
import com.tm7xco.springkeycloakpoc.keycloak.dto.KeycloakRealmRoleResponse;
import com.tm7xco.springkeycloakpoc.keycloak.dto.KeycloakTokenResponse;
import com.tm7xco.springkeycloakpoc.keycloak.dto.KeycloakUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
            response = keycloakApi.createUser(username, password, enabled,
                    keycloakApiConfig.getUserRealm(), getAdminBearerToken());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return (response != null && response.getStatusCode().is2xxSuccessful()); // user created in keycloak
    }

    public String deleteKeycloakUser(String userId) {
        ResponseEntity<String> response = null;
        String username = null;

        try {
            username = getUsernameById(userId);
            response = keycloakApi.deleteUser(userId, keycloakApiConfig.getUserRealm(), getAdminBearerToken());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            username = null;
        }

        return username;
    }

    public boolean updateUserPassword(String userId, String newPassword) {
        ResponseEntity<String> response = null;

        try {
            response = keycloakApi.updatePassword(userId, newPassword, keycloakApiConfig.getUserRealm(), getAdminBearerToken());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return (response != null && response.getStatusCode().is2xxSuccessful()); // user password updated in keycloak
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
                        keycloakApiConfig.getUserRealm(), getAdminBearerToken());
            } else {
                response = keycloakApi.deleteRealmRoleFromUser(userId, getRealmRoleId(roleName), roleName,
                        keycloakApiConfig.getUserRealm(), getAdminBearerToken());
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
                keycloakApiConfig.getAdminUsername(),
                keycloakApiConfig.getAdminPassword(),
                keycloakApiConfig.getAdminClientId());
    }

    private String getUsernameById(String userId) {
        ResponseEntity<KeycloakUserResponse> response = keycloakApi.getUserById(
                userId,
                keycloakApiConfig.getUserRealm(),
                getAdminBearerToken());
        return Objects.requireNonNull(response.getBody()).getUsername();
    }

    private String getRealmRoleId(String roleName) {
        ResponseEntity<KeycloakRealmRoleResponse> response = keycloakApi.getRealmRoleByName(
                roleName,
                keycloakApiConfig.getUserRealm(),
                getAdminBearerToken());
        return Objects.requireNonNull(response.getBody()).getId();
    }

}
