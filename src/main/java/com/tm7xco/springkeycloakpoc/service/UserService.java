package com.tm7xco.springkeycloakpoc.service;

import com.tm7xco.springkeycloakpoc.controller.dto.LoginRequest;
import com.tm7xco.springkeycloakpoc.controller.dto.LoginResponse;
import com.tm7xco.springkeycloakpoc.controller.dto.RegisterRequest;
import com.tm7xco.springkeycloakpoc.domain.AppUser;
import com.tm7xco.springkeycloakpoc.keycloak.KeycloakService;
import com.tm7xco.springkeycloakpoc.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    public AppUser registerUser(RegisterRequest registerRequest) {
        if (isUsernameExist(registerRequest.getUsername())) {
            log.info("User not created: username is already existing!");
            return null;
        }

        if (!keycloakService.createKeycloakUser(registerRequest.getUsername(), registerRequest.getPassword(), true)) {
            log.error("User not created: error during creating user in Keycloak");
            return null;
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
        LoginResponse loginResponse = null;

        AppUser user = userRepository.findByUsername(loginRequest.getUsername());

        if (user == null) {
            log.info("Username not existing in database!");
            return null;
        }

        String token = keycloakService.getUserBearerToken(user.getUsername(), loginRequest.getPassword());

        if (token != null) {
            loginResponse = new LoginResponse(user.getUsername(), token);
            log.info("User authenticated successfully!");
        } else {
            log.info("User authentication with Keycloak failed!");
        }

        return loginResponse;
    }

    private boolean isUsernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

}
