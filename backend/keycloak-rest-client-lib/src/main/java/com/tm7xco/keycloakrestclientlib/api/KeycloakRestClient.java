package com.tm7xco.keycloakrestclientlib.api;

import com.tm7xco.keycloakrestclientlib.dto.KeycloakRealmRoleResponse;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakTokenResponse;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface KeycloakRestClient {

    ResponseEntity<KeycloakTokenResponse> getAdminBearerToken();

    ResponseEntity<KeycloakTokenResponse> getUserBearerToken(String username, String password);

    ResponseEntity<KeycloakUserResponse> getUserById(String userId, String bearerToken);

    ResponseEntity<String> createUser(String username, String password, boolean enabled, String bearerToken);

    ResponseEntity<String> deleteUser(String userId, String bearerToken);

    ResponseEntity<String> updatePassword(String userId, String newPassword, String bearerToken);

    ResponseEntity<KeycloakRealmRoleResponse> getRealmRoleByName(String roleName, String bearerToken);

    ResponseEntity<String> addRealmRoleToUser(String userId, String roleId, String roleName, String bearerToken);

    ResponseEntity<String> deleteRealmRoleFromUser(String userId, String roleId, String roleName, String bearerToken);

}
