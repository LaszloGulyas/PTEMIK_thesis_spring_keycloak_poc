package com.tm7xco.keycloakrestclientlib.api;

import com.tm7xco.keycloakrestclientlib.autoconfigure.KeycloakRestClientConfigProperties;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakTokenResponse;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakUserCredentialsRequest;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class KeycloakRestClientImpl1902Test {

    private static final String TEST_TOKEN = "test-token";
    private static final String TEST_ADMIN_USER = "test-admin-user";
    private static final String TEST_ADMIN_PASSWORD = "test-admin-password";
    private static final String TEST_ADMIN_CLIENT = "test-admin-client";
    private static final String TEST_USERNAME = "test-username";
    private static final String TEST_PASSWORD = "test-password";
    private static final String TEST_CLIENT = "test-client";
    private static final String TEST_ID = "123456";

    @Mock
    private RestTemplate restTemplate;

    @Captor
    private ArgumentCaptor<HttpMethod> methodCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<?>> requestCaptor;

    private KeycloakRestClientConfigProperties keycloakClientConfig;
    private KeycloakRestClientImpl1902 underTest;

    @BeforeEach
    void init() {
        keycloakClientConfig = new KeycloakRestClientConfigProperties();
        keycloakClientConfig.setAdmin(new KeycloakRestClientConfigProperties.Admin());
        underTest = new KeycloakRestClientImpl1902(restTemplate, keycloakClientConfig);
    }

    @Test
    void testGetAdminBearerToken() {
        // given
        keycloakClientConfig.getAdmin().setUsername(TEST_ADMIN_USER);
        keycloakClientConfig.getAdmin().setPassword(TEST_ADMIN_PASSWORD);
        keycloakClientConfig.getAdmin().setClientId(TEST_ADMIN_CLIENT);

        ResponseEntity<KeycloakTokenResponse> expectedResponse = ResponseEntity.ok(KeycloakTokenResponse.builder().accessToken(TEST_TOKEN).build());
        doReturn(expectedResponse).when(restTemplate).exchange(anyString(), any(), any(), ArgumentMatchers.<Class<KeycloakTokenResponse>>any());

        // when
        ResponseEntity<KeycloakTokenResponse> result = underTest.getAdminBearerToken();

        // then
        verify(restTemplate).exchange(anyString(), methodCaptor.capture(), requestCaptor.capture(), ArgumentMatchers.<Class<KeycloakTokenResponse>>any());
        MultiValueMap<String, String> requestHeaders = requestCaptor.getValue().getHeaders();
        MultiValueMap<String, String> requestBody = (MultiValueMap<String, String>) requestCaptor.getValue().getBody();

        assertAll(
                () -> assertEquals(TEST_TOKEN, result.getBody().getAccessToken()),
                () -> assertEquals(HttpMethod.POST, methodCaptor.getValue()),
                () -> assertEquals(MediaType.APPLICATION_FORM_URLENCODED.toString(), requestHeaders.getFirst("content-type")),
                () -> assertEquals(TEST_ADMIN_USER, requestBody.getFirst("username")),
                () -> assertEquals(TEST_ADMIN_PASSWORD, requestBody.getFirst("password")),
                () -> assertEquals(TEST_ADMIN_CLIENT, requestBody.getFirst("client_id"))
        );
    }

    @Test
    void testGetUserBearerToken() {
        // given
        keycloakClientConfig.setClientId(TEST_CLIENT);
        ResponseEntity<KeycloakTokenResponse> expectedResponse = ResponseEntity.ok(KeycloakTokenResponse.builder().accessToken(TEST_TOKEN).build());
        doReturn(expectedResponse).when(restTemplate).exchange(anyString(), any(), any(), ArgumentMatchers.<Class<KeycloakTokenResponse>>any());

        // when
        ResponseEntity<KeycloakTokenResponse> result = underTest.getUserBearerToken(TEST_USERNAME, TEST_PASSWORD);

        // then
        verify(restTemplate).exchange(anyString(), methodCaptor.capture(), requestCaptor.capture(), ArgumentMatchers.<Class<KeycloakTokenResponse>>any());
        MultiValueMap<String, String> requestHeaders = requestCaptor.getValue().getHeaders();
        MultiValueMap<String, String> requestBody = (MultiValueMap<String, String>) requestCaptor.getValue().getBody();

        assertAll(
                () -> assertEquals(TEST_TOKEN, result.getBody().getAccessToken()),
                () -> assertEquals(HttpMethod.POST, methodCaptor.getValue()),
                () -> assertEquals(MediaType.APPLICATION_FORM_URLENCODED.toString(), requestHeaders.getFirst("content-type")),
                () -> assertEquals(TEST_USERNAME, requestBody.getFirst("username")),
                () -> assertEquals(TEST_PASSWORD, requestBody.getFirst("password")),
                () -> assertEquals(TEST_CLIENT, requestBody.getFirst("client_id"))
        );
    }

    @Test
    void testGetUserById() {
        // given
        ResponseEntity<KeycloakUserResponse> expectedResponse = ResponseEntity.ok(KeycloakUserResponse.builder().username(TEST_USERNAME).build());
        doReturn(expectedResponse).when(restTemplate).exchange(anyString(), any(), any(), ArgumentMatchers.<Class<KeycloakUserResponse>>any());

        // when
        ResponseEntity<KeycloakUserResponse> result = underTest.getUserById(TEST_ID, TEST_TOKEN);

        // then
        verify(restTemplate).exchange(anyString(), methodCaptor.capture(), requestCaptor.capture(), ArgumentMatchers.<Class<KeycloakUserResponse>>any());
        MultiValueMap<String, String> requestHeaders = requestCaptor.getValue().getHeaders();

        assertAll(
                () -> assertEquals(TEST_USERNAME, result.getBody().getUsername()),
                () -> assertEquals(HttpMethod.GET, methodCaptor.getValue()),
                () -> assertEquals("Bearer " + TEST_TOKEN, requestHeaders.getFirst("authorization"))
        );
    }

    @Test
    void testCreateUser() {
        // given
        ResponseEntity<String> expectedResponse = ResponseEntity.ok().build();
        doReturn(expectedResponse).when(restTemplate).exchange(anyString(), any(), any(), ArgumentMatchers.<Class<String>>any());

        // when
        underTest.createUser(TEST_USERNAME, TEST_PASSWORD, true, TEST_TOKEN);

        // then
        verify(restTemplate).exchange(anyString(), methodCaptor.capture(), requestCaptor.capture(), ArgumentMatchers.<Class<KeycloakTokenResponse>>any());
        MultiValueMap<String, String> requestHeaders = requestCaptor.getValue().getHeaders();
        Map<String, Object> requestBody = (Map<String, Object>) requestCaptor.getValue().getBody();
        List<KeycloakUserCredentialsRequest> credentials = (List<KeycloakUserCredentialsRequest>) requestBody.get("credentials");

        assertAll(
                () -> assertEquals(HttpMethod.POST, methodCaptor.getValue()),
                () -> assertEquals("Bearer " + TEST_TOKEN, requestHeaders.getFirst("authorization")),
                () -> assertEquals(MediaType.APPLICATION_JSON.toString(), requestHeaders.getFirst("content-type")),
                () -> assertEquals(TEST_USERNAME, requestBody.get("username")),
                () -> assertEquals(TEST_PASSWORD, credentials.get(0).getValue()),
                () -> assertEquals(true, requestBody.get("enabled"))
        );
    }

    @Test
    void testDeleteUser() {
        // given
        ResponseEntity<String> expectedResponse = ResponseEntity.ok().build();
        doReturn(expectedResponse).when(restTemplate).exchange(anyString(), any(), any(), ArgumentMatchers.<Class<String>>any());

        // when
        underTest.deleteUser(TEST_ID, TEST_TOKEN);

        // then
        verify(restTemplate).exchange(anyString(), methodCaptor.capture(), requestCaptor.capture(), ArgumentMatchers.<Class<KeycloakTokenResponse>>any());
        MultiValueMap<String, String> requestHeaders = requestCaptor.getValue().getHeaders();

        assertAll(
                () -> assertEquals(HttpMethod.DELETE, methodCaptor.getValue()),
                () -> assertEquals("Bearer " + TEST_TOKEN, requestHeaders.getFirst("authorization"))
        );
    }

    @Test
    void testUpdatePassword() {
        // given
        ResponseEntity<String> expectedResponse = ResponseEntity.ok().build();
        doReturn(expectedResponse).when(restTemplate).exchange(anyString(), any(), any(), ArgumentMatchers.<Class<String>>any());

        // when
        underTest.updatePassword(TEST_ID, TEST_PASSWORD, TEST_TOKEN);

        // then
        verify(restTemplate).exchange(anyString(), methodCaptor.capture(), requestCaptor.capture(), ArgumentMatchers.<Class<KeycloakTokenResponse>>any());
        MultiValueMap<String, String> requestHeaders = requestCaptor.getValue().getHeaders();
        KeycloakUserCredentialsRequest requestBody = (KeycloakUserCredentialsRequest) requestCaptor.getValue().getBody();

        assertAll(
                () -> assertEquals(HttpMethod.PUT, methodCaptor.getValue()),
                () -> assertEquals("Bearer " + TEST_TOKEN, requestHeaders.getFirst("authorization")),
                () -> assertEquals(MediaType.APPLICATION_JSON.toString(), requestHeaders.getFirst("content-type")),
                () -> assertEquals(TEST_PASSWORD, requestBody.getValue())
        );
    }
}
