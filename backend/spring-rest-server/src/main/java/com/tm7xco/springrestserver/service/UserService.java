package com.tm7xco.springrestserver.service;

import com.tm7xco.keycloakrestclientlib.KeycloakService;
import com.tm7xco.springrestserver.controller.dto.LoginRequest;
import com.tm7xco.springrestserver.controller.dto.LoginResponse;
import com.tm7xco.springrestserver.controller.dto.RegisterRequest;
import com.tm7xco.springrestserver.controller.dto.UpdatePasswordRequest;
import com.tm7xco.springrestserver.domain.AppUser;
import com.tm7xco.springrestserver.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    public AppUser registerUser(RegisterRequest registerRequest) {
        if (isUsernameExist(registerRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User not created: username is already existing!");
        }

        if (!keycloakService.createKeycloakUser(registerRequest.getUsername(), registerRequest.getPassword(), true)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not created: error during creating user in Keycloak");
        }

        log.info("Creating new user started...");
        AppUser user = AppUser.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .build();

        AppUser savedUser = userRepository.save(user);
        log.info("User created and saved in database and keycloak!");

        return savedUser;
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        log.info("User authentication is started...");

        if (!isUsernameExist(loginRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not existing in database.");
        }

        String token = keycloakService.getUserBearerToken(loginRequest.getUsername(), loginRequest.getPassword());
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed.");
        }

        log.info("User authenticated successfully!");
        return new LoginResponse(loginRequest.getUsername(), token);
    }

    public void deleteUser() {
        log.info("User deletion is started...");

        String authenticatedUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        String usernameToDelete = keycloakService.getUsernameById(authenticatedUserId);
        if (usernameToDelete == null || !isUsernameExist(usernameToDelete)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not existing in database.");
        }

        userRepository.deleteAppUserByUsername(usernameToDelete);
        keycloakService.deleteKeycloakUser(authenticatedUserId);
        log.info("User deleted successfully!");
    }

    public void updateUserPassword(UpdatePasswordRequest passwordRequest) {
        log.info("Updating user password is started...");

        String authenticatedUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        String usernameToUpdatePassword = keycloakService.getUsernameById(authenticatedUserId);
        if (usernameToUpdatePassword == null || !isUsernameExist(usernameToUpdatePassword)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not existing in database.");
        }

        boolean isUpdated = keycloakService.updateUserPassword(authenticatedUserId, passwordRequest.getPassword());

        if (!isUpdated) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error during password update.");
        }

        log.info("User password updated successfully!");
    }

    private boolean isUsernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

}
