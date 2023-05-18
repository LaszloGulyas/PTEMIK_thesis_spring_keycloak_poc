package com.tm7xco.keycloakrestclientlib;

import com.tm7xco.keycloakrestclientlib.api.KeycloakRestClient;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakTokenResponse;
import com.tm7xco.keycloakrestclientlib.dto.KeycloakUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakServiceImplTest {

    private static final String TEST_TOKEN = "test-token";
    private static final String TEST_ID = "123456";
    private static final String TEST_USERNAME = "username";
    private static final String TEST_PASSWORD = "password";


    @Mock
    private KeycloakRestClient keycloakRestClient;

    private KeycloakServiceImpl underTest;

    @BeforeEach
    void init() {
        underTest = new KeycloakServiceImpl(keycloakRestClient);
    }

    @Test
    void testGetUserBearerToken_whenSuccessful() {
        // given
        when(keycloakRestClient.getUserBearerToken(TEST_USERNAME, TEST_PASSWORD))
                .thenReturn(createKeycloakResponseWithToken(TEST_TOKEN));

        // when
        String result = underTest.getUserBearerToken(TEST_USERNAME, TEST_PASSWORD);

        // then
        assertEquals(TEST_TOKEN, result);
    }

    @Test
    void testGetUserBearerToken_whenAuthenticationFailed() {
        // given
        when(keycloakRestClient.getUserBearerToken(TEST_USERNAME, TEST_PASSWORD))
                .thenReturn(createKeycloakResponseWithToken(null));

        // when
        assertThrows(RuntimeException.class, () -> underTest.getUserBearerToken(TEST_USERNAME, TEST_PASSWORD));
    }

    @Test
    void testGetUsernameById_whenSuccessful() {
        // given
        ResponseEntity<KeycloakUserResponse> testResponse =
                ResponseEntity.ok(KeycloakUserResponse.builder().username(TEST_USERNAME).build());
        when(keycloakRestClient.getAdminBearerToken()).thenReturn(createKeycloakResponseWithToken(TEST_TOKEN));
        when(keycloakRestClient.getUserById(TEST_ID, TEST_TOKEN)).thenReturn(testResponse);

        // when
        String result = underTest.getUsernameById(TEST_ID);

        // then
        assertEquals(TEST_USERNAME, result);
    }

    @Test
    void testGetUsernameById_whenUsernameNotFound() {
        // given
        when(keycloakRestClient.getAdminBearerToken()).thenReturn(createKeycloakResponseWithToken(TEST_TOKEN));
        when(keycloakRestClient.getUserById(TEST_ID, TEST_TOKEN)).thenReturn(null);

        // when
        String result = underTest.getUsernameById(TEST_ID);

        // then
        assertNull(result);
    }

    @Test
    void testCreateKeycloakUser_whenSuccessful() {
        // given
        when(keycloakRestClient.getAdminBearerToken()).thenReturn(createKeycloakResponseWithToken(TEST_TOKEN));
        when(keycloakRestClient.createUser(TEST_USERNAME, TEST_PASSWORD, true, TEST_TOKEN))
                .thenReturn(ResponseEntity.ok().build());

        // when
        boolean result = underTest.createKeycloakUser(TEST_USERNAME, TEST_PASSWORD, true);

        // then
        assertTrue(result);
    }

    @Test
    void testCreateKeycloakUser_whenUserCreationFailed() {
        // given
        when(keycloakRestClient.getAdminBearerToken()).thenReturn(createKeycloakResponseWithToken(TEST_TOKEN));
        when(keycloakRestClient.createUser(TEST_USERNAME, TEST_PASSWORD, true, TEST_TOKEN))
                .thenReturn(ResponseEntity.badRequest().build());

        // when
        boolean result = underTest.createKeycloakUser(TEST_USERNAME, TEST_PASSWORD, true);

        // then
        assertFalse(result);
    }

    @Test
    void testDeleteKeycloakUser_whenSuccessful() {
        // given
        when(keycloakRestClient.getAdminBearerToken()).thenReturn(createKeycloakResponseWithToken(TEST_TOKEN));
        when(keycloakRestClient.deleteUser(TEST_ID, TEST_TOKEN)).thenReturn(ResponseEntity.ok().build());

        // when
        boolean result = underTest.deleteKeycloakUser(TEST_ID);

        //then
        assertTrue(result);
    }

    @Test
    void testDeleteKeycloakUser_whenUserDeletionFailed() {
        // given
        when(keycloakRestClient.getAdminBearerToken()).thenReturn(createKeycloakResponseWithToken(TEST_TOKEN));
        when(keycloakRestClient.deleteUser(TEST_ID, TEST_TOKEN)).thenReturn(ResponseEntity.badRequest().build());

        // when
        boolean result = underTest.deleteKeycloakUser(TEST_ID);

        //then
        assertFalse(result);
    }

    @Test
    void testUpdateUserPassword_whenSuccessful() {
        // given
        when(keycloakRestClient.getAdminBearerToken()).thenReturn(createKeycloakResponseWithToken(TEST_TOKEN));
        when(keycloakRestClient.updatePassword(TEST_ID, TEST_PASSWORD, TEST_TOKEN))
                .thenReturn(ResponseEntity.ok().build());

        // when
        boolean result = underTest.updateUserPassword(TEST_ID, TEST_PASSWORD);

        // then
        assertTrue(result);
    }

    @Test
    void testUpdateUserPassword_whenUpdateFailed() {
        // given
        when(keycloakRestClient.getAdminBearerToken()).thenReturn(createKeycloakResponseWithToken(TEST_TOKEN));
        when(keycloakRestClient.updatePassword(TEST_ID, TEST_PASSWORD, TEST_TOKEN))
                .thenReturn(ResponseEntity.badRequest().build());

        // when
        boolean result = underTest.updateUserPassword(TEST_ID, TEST_PASSWORD);

        // then
        assertFalse(result);
    }

    private ResponseEntity<KeycloakTokenResponse> createKeycloakResponseWithToken(String token) {
        return ResponseEntity.ok(KeycloakTokenResponse.builder().accessToken(token).build());
    }

}
