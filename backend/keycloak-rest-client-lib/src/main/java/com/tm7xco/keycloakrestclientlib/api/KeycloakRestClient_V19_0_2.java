package com.tm7xco.keycloakrestclientlib.api;

import com.tm7xco.keycloakrestclientlib.autoconfigure.KeycloakRestClientConfigProperties;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakRealmRoleResponse;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakTokenResponse;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakUserResponse;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakRealmRoleRequest;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakUserCredentialsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class KeycloakRestClient_V19_0_2 implements KeycloakRestClient {

    private final RestTemplate restTemplate;
    private final KeycloakRestClientConfigProperties keycloakClientConfig;

    public ResponseEntity<KeycloakTokenResponse> getAdminBearerToken() {
        log.info("Preparing rest-call to Keycloak: getAdminBearerToken");

        String url = keycloakClientConfig.getUrl() + "/realms/master/protocol/openid-connect/token";
        return getBearerTokenByPassword(
                url,
                keycloakClientConfig.getAdmin().getUsername(),
                keycloakClientConfig.getAdmin().getPassword(),
                keycloakClientConfig.getAdmin().getClientId());
    }

    public ResponseEntity<KeycloakTokenResponse> getUserBearerToken(String username, String password) {
        log.info("Preparing rest-call to Keycloak: getUserBearerToken");

        String url = keycloakClientConfig.getUrl() + "/realms/" + keycloakClientConfig.getRealm() + "/protocol/openid-connect/token";
        return getBearerTokenByPassword(url, username, password, keycloakClientConfig.getClientId());
    }

    public ResponseEntity<KeycloakUserResponse> getUserById(String userId, String bearerToken) {
        log.info("Preparing rest-call to Keycloak: getUserById");

        String url = keycloakClientConfig.getUrl() + "/admin/realms/" + keycloakClientConfig.getRealm() + "/users/" + userId;
        HttpMethod method = HttpMethod.GET;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        log.info("Initiating rest-call to Keycloak: getUserById");
        return restTemplate.exchange(url, method, request, KeycloakUserResponse.class);
    }

    public ResponseEntity<String> createUser(String username, String password, boolean enabled, String bearerToken) {
        log.info("Preparing rest-call to Keycloak: createUser");

        String url = keycloakClientConfig.getUrl() + "/admin/realms/" + keycloakClientConfig.getRealm() + "/users";
        HttpMethod method = HttpMethod.POST;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        KeycloakUserCredentialsRequest credentials = KeycloakUserCredentialsRequest.builder()
                .type("password")
                .value(password)
                .temporary(false)
                .build();

        Map<String, Object> jsonBody = Map.of(
                "username", username,
                "enabled", enabled,
                "credentials", List.of(credentials)
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(jsonBody, headers);

        log.info("Initiating rest-call to Keycloak: createUser");
        return restTemplate.exchange(url, method, request, String.class);
    }

    public ResponseEntity<String> deleteUser(String userId, String bearerToken) {
        log.info("Preparing rest-call to Keycloak: deleteUser");

        String url = keycloakClientConfig.getUrl() + "/admin/realms/" + keycloakClientConfig.getRealm() + "/users/" + userId;
        HttpMethod method = HttpMethod.DELETE;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        log.info("Initiating rest-call to Keycloak: deleteUser");
        return restTemplate.exchange(url, method, request, String.class);
    }

    public ResponseEntity<String> updatePassword(String userId, String newPassword, String bearerToken) {
        log.info("Preparing rest-call to Keycloak: updatePassword");

        String url = keycloakClientConfig.getUrl() + "/admin/realms/" + keycloakClientConfig.getRealm() + "/users/" + userId + "/reset-password";
        HttpMethod method = HttpMethod.PUT;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        KeycloakUserCredentialsRequest credentials = KeycloakUserCredentialsRequest.builder()
                .type("password")
                .value(newPassword)
                .temporary(false)
                .build();

        HttpEntity<KeycloakUserCredentialsRequest> request = new HttpEntity<>(credentials, headers);

        log.info("Initiating rest-call to Keycloak: updatePassword");
        return restTemplate.exchange(url, method, request, String.class);
    }

    public ResponseEntity<KeycloakRealmRoleResponse> getRealmRoleByName(String roleName, String bearerToken) {
        log.info("Preparing rest-call to Keycloak: getRealmRoleByName");

        String url = keycloakClientConfig.getUrl() + "/admin/realms/" + keycloakClientConfig.getRealm() + "/roles/" + roleName;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);

        HttpEntity<KeycloakUserCredentialsRequest> request = new HttpEntity<>(headers);

        log.info("Initiating rest-call to Keycloak: getRealmRoleByName");
        return restTemplate.exchange(url, HttpMethod.GET, request, KeycloakRealmRoleResponse.class);
    }

    public ResponseEntity<String> addRealmRoleToUser(String userId, String roleId, String roleName, String bearerToken) {
        return requestRealmRoleOperation(HttpMethod.POST, userId, roleId, roleName, bearerToken);
    }

    public ResponseEntity<String> deleteRealmRoleFromUser(String userId, String roleId, String roleName, String bearerToken) {
        return requestRealmRoleOperation(HttpMethod.DELETE, userId, roleId, roleName, bearerToken);
    }

    private ResponseEntity<KeycloakTokenResponse> getBearerTokenByPassword(String url, String username, String password, String clientId) {
        HttpMethod method = HttpMethod.POST;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formEncodedBody = new LinkedMultiValueMap<>();
        formEncodedBody.add("username", username);
        formEncodedBody.add("password", password);
        formEncodedBody.add("grant_type", "password");
        formEncodedBody.add("client_id", clientId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formEncodedBody, headers);

        log.info("Initiating rest-call to Keycloak: getBearerTokenByPassword");
        return restTemplate.exchange(url, method, request, KeycloakTokenResponse.class);
    }

    private ResponseEntity<String> requestRealmRoleOperation(HttpMethod method, String userId, String roleId, String roleName, String bearerToken) {
        log.info("Preparing rest-call to Keycloak: requestRealmRoleOperation " + method);

        String url = keycloakClientConfig.getUrl() + "/admin/realms/" + keycloakClientConfig.getRealm() + "/users/" + userId + "/role-mappings/realm";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        KeycloakRealmRoleRequest role = KeycloakRealmRoleRequest.builder()
                .id(roleId)
                .name(roleName)
                .build();

        List<KeycloakRealmRoleRequest> listOfRoles = List.of(role);

        HttpEntity<List<KeycloakRealmRoleRequest>> request = new HttpEntity<>(listOfRoles, headers);

        log.info("Initiating rest-call to Keycloak: requestRealmRoleOperation " + method);
        return restTemplate.exchange(url, method, request, String.class);
    }

}
