package com.tm7xco.springrestserver.service;

import com.tm7xco.keycloakrestclientlib.KeycloakService;
import com.tm7xco.springrestserver.controller.dto.LoginRequest;
import com.tm7xco.springrestserver.controller.dto.LoginResponse;
import com.tm7xco.springrestserver.controller.dto.RegisterRequest;
import com.tm7xco.springrestserver.controller.dto.UpdatePasswordRequest;
import com.tm7xco.springrestserver.domain.AppUser;
import com.tm7xco.springrestserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String TEST_USERNAME = "testUsername";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_EMAIL = "test@email.com";

    private static final RegisterRequest TEST_REGISTER_REQUEST = new RegisterRequest(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL);
    private static final LoginRequest TEST_LOGIN_REQUEST = new LoginRequest(TEST_USERNAME, TEST_PASSWORD);
    private static final UpdatePasswordRequest TEST_UPDATE_PASSWORD_REQUEST = new UpdatePasswordRequest(TEST_PASSWORD);

    private static final AppUser TEST_APP_USER = AppUser.builder().username(TEST_USERNAME).email(TEST_EMAIL).build();

    @Mock
    private UserRepository userRepository;
    @Mock
    private KeycloakService keycloakService;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    private UserService underTest;

    @BeforeEach
    void init() {
        underTest = new UserService(userRepository, keycloakService);
    }

    @Test
    void testRegisterUser_whenSuccessful() {
        // given
        when(keycloakService.createKeycloakUser(TEST_USERNAME, TEST_PASSWORD, true)).thenReturn(true);

        // when
        underTest.registerUser(TEST_REGISTER_REQUEST);

        //then
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(userRepository).save(TEST_APP_USER);
    }

    @Test
    void testRegisterUser_whenUsernameExists() {
        // given
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(TEST_APP_USER);

        // when
        ResponseStatusException actualException = assertThrows(ResponseStatusException.class,
                () -> underTest.registerUser(TEST_REGISTER_REQUEST));

        // then
        assertEquals(HttpStatus.CONFLICT, actualException.getStatusCode());
    }

    @Test
    void testRegisterUser_whenKeycloakFailedCreatingUser() {
        // given
        when(keycloakService.createKeycloakUser(TEST_USERNAME, TEST_PASSWORD, true)).thenReturn(false);

        // when
        ResponseStatusException actualException = assertThrows(ResponseStatusException.class,
                () -> underTest.registerUser(TEST_REGISTER_REQUEST));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualException.getStatusCode());
    }

    @Test
    void testLoginUser_whenSuccessful() {
        // given
        String expectedToken = "testToken";
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(TEST_APP_USER);
        when(keycloakService.getUserBearerToken(TEST_USERNAME, TEST_PASSWORD)).thenReturn(expectedToken);

        // when
        LoginResponse result = underTest.loginUser(TEST_LOGIN_REQUEST);

        //then
        assertEquals(TEST_USERNAME, result.getUsername());
        assertEquals(expectedToken, result.getToken());
    }

    @Test
    void testLoginUser_whenUsernameNotExists() {
        // given
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(null);

        // when
        ResponseStatusException actualException = assertThrows(ResponseStatusException.class,
                () -> underTest.loginUser(TEST_LOGIN_REQUEST));
        //then
        assertEquals(HttpStatus.NOT_FOUND, actualException.getStatusCode());
    }

    @Test
    void testLoginUser_whenKeycloakAuthenticationFailed() {
        // given
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(TEST_APP_USER);
        when(keycloakService.getUserBearerToken(TEST_USERNAME, TEST_PASSWORD)).thenReturn(null);

        // when
        ResponseStatusException actualException = assertThrows(ResponseStatusException.class,
                () -> underTest.loginUser(TEST_LOGIN_REQUEST));

        //then
        assertEquals(HttpStatus.UNAUTHORIZED, actualException.getStatusCode());
    }

    @Test
    void testDeleteUser_whenSuccessful() {
        // given
        mockSecurityContext(TEST_USERNAME);
        when(keycloakService.getUsernameById(TEST_USERNAME)).thenReturn(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(TEST_APP_USER);

        // when
        underTest.deleteUser();

        //then
        verify(userRepository).deleteAppUserByUsername(TEST_USERNAME);
        verify(keycloakService).deleteKeycloakUser(TEST_USERNAME);
    }

    @Test
    void testDeleteUser_whenUsernameNotFoundByKeycloak() {
        // given
        mockSecurityContext(TEST_USERNAME);
        when(keycloakService.getUsernameById(TEST_USERNAME)).thenReturn(null);

        // when
        ResponseStatusException actualException = assertThrows(ResponseStatusException.class,
                () -> underTest.deleteUser());

        //then
        assertEquals(HttpStatus.NOT_FOUND, actualException.getStatusCode());
    }

    @Test
    void testDeleteUser_whenUsernameNotFoundInDatabase() {
        // given
        mockSecurityContext(TEST_USERNAME);
        when(keycloakService.getUsernameById(TEST_USERNAME)).thenReturn(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(null);

        // when
        ResponseStatusException actualException = assertThrows(ResponseStatusException.class,
                () -> underTest.deleteUser());

        //then
        assertEquals(HttpStatus.NOT_FOUND, actualException.getStatusCode());
    }

    @Test
    void testUpdateUserPassword_whenSuccessful() {
        // given
        mockSecurityContext(TEST_USERNAME);
        when(keycloakService.getUsernameById(TEST_USERNAME)).thenReturn(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(TEST_APP_USER);
        when(keycloakService.updateUserPassword(TEST_USERNAME, TEST_PASSWORD)).thenReturn(true);

        // when
        assertDoesNotThrow(() -> underTest.updateUserPassword(TEST_UPDATE_PASSWORD_REQUEST));
    }

    @Test
    void testUpdateUserPassword_whenKeycloakUpdateFailed() {
        // given
        mockSecurityContext(TEST_USERNAME);
        when(keycloakService.getUsernameById(TEST_USERNAME)).thenReturn(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(TEST_APP_USER);
        when(keycloakService.updateUserPassword(TEST_USERNAME, TEST_PASSWORD)).thenReturn(false);

        // when
        ResponseStatusException actualException = assertThrows(ResponseStatusException.class,
                () -> underTest.updateUserPassword(TEST_UPDATE_PASSWORD_REQUEST));

        //then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualException.getStatusCode());
    }

    @Test
    void testUpdatePassword_whenUsernameNotFoundByKeycloak() {
        // given
        mockSecurityContext(TEST_USERNAME);
        when(keycloakService.getUsernameById(TEST_USERNAME)).thenReturn(null);

        // when
        ResponseStatusException actualException = assertThrows(ResponseStatusException.class,
                () -> underTest.updateUserPassword(TEST_UPDATE_PASSWORD_REQUEST));

        //then
        assertEquals(HttpStatus.NOT_FOUND, actualException.getStatusCode());
    }

    @Test
    void testUpdatePassword_whenUsernameNotFoundInDatabase() {
        // given
        mockSecurityContext(TEST_USERNAME);
        when(keycloakService.getUsernameById(TEST_USERNAME)).thenReturn(TEST_USERNAME);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(null);

        // when
        ResponseStatusException actualException = assertThrows(ResponseStatusException.class,
                () -> underTest.updateUserPassword(TEST_UPDATE_PASSWORD_REQUEST));

        //then
        assertEquals(HttpStatus.NOT_FOUND, actualException.getStatusCode());
    }

    private void mockSecurityContext(String username) {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
    }

}
