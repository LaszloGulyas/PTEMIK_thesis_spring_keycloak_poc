package com.tm7xco.springkeycloakpoc.keycloak;

import com.tm7xco.springkeycloakpoc.config.KeycloakApiConfig;
import com.tm7xco.springkeycloakpoc.keycloak.dto.KeycloakTokenResponse;
import com.tm7xco.springkeycloakpoc.keycloak.dto.KeycloakUserResponse;
import com.tm7xco.springkeycloakpoc.keycloak.model.KeycloakUserCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakApi {

    private final RestTemplate restTemplate;
    private final KeycloakApiConfig keycloakApiConfig;

    public ResponseEntity<KeycloakTokenResponse> getBearerTokenByPassword(String url, String username, String password, String clientId) {
        log.info("Preparing rest-call to Keycloak: getBearerTokenByPassword");

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

    public ResponseEntity<KeycloakUserResponse> getUserById(String userId, String realm, String bearerToken) {
        log.info("Preparing rest-call to Keycloak: getUserById");

        String url = keycloakApiConfig.getUrl() + "/admin/realms/" + realm + "/users/" + userId;
        HttpMethod method = HttpMethod.GET;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        log.info("Initiating rest-call to Keycloak: getUserById");

        return restTemplate.exchange(url, method, request, KeycloakUserResponse.class);
    }

    public ResponseEntity<String> createUser(String username, String password, boolean enabled, String realm, String bearerToken) {
        log.info("Preparing rest-call to Keycloak: createUser");

        String url = keycloakApiConfig.getUrl() + "/admin/realms/" + realm + "/users";
        HttpMethod method = HttpMethod.POST;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        KeycloakUserCredentials credentials = KeycloakUserCredentials.builder()
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

    public ResponseEntity<String> deleteUser(String userId, String realm, String bearerToken) {
        log.info("Preparing rest-call to Keycloak: deleteUser");

        String url = keycloakApiConfig.getUrl() + "/admin/realms/" + realm + "/users/" + userId;
        HttpMethod method = HttpMethod.DELETE;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        log.info("Initiating rest-call to Keycloak: deleteUser");
        return restTemplate.exchange(url, method, request, String.class);
    }

    public ResponseEntity<String> updatePassword(String userId, String newPassword, String realm, String bearerToken) {
        log.info("Preparing rest-call to Keycloak: updatePassword");

        String url = keycloakApiConfig.getUrl() + "/admin/realms/" + realm + "/users/" + userId + "/reset-password";
        HttpMethod method = HttpMethod.PUT;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        KeycloakUserCredentials credentials = KeycloakUserCredentials.builder()
                .type("password")
                .value(newPassword)
                .temporary(false)
                .build();

        HttpEntity<KeycloakUserCredentials> request = new HttpEntity<>(credentials, headers);

        log.info("Initiating rest-call to Keycloak: updatePassword");
        return restTemplate.exchange(url, method, request, String.class);
    }

}
