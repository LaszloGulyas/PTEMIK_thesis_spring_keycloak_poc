package com.tm7xco.springkeycloakpoc.service;

import com.tm7xco.springkeycloakpoc.controller.dto.LoginRequest;
import com.tm7xco.springkeycloakpoc.controller.dto.RegisterRequest;
import com.tm7xco.springkeycloakpoc.domain.AppUser;
import com.tm7xco.springkeycloakpoc.keycloak.KeycloakService;
import com.tm7xco.springkeycloakpoc.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptEncoder;
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
                .password(bCryptEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .build();

        AppUser savedUser = userRepository.save(user);
        log.info("User created and saved in database and keycloak!");

        return savedUser;
    }

    public AppUser loginUser(LoginRequest loginRequest) {
        log.info("User authentication is started...");

        AppUser user = userRepository.findByUsername(loginRequest.getUsername());
        AppUser authenticatedUser = null;
        if (user != null && bCryptEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            authenticatedUser = user;
        }

        log.info("User authentication is finished!");
        return authenticatedUser;
    }

    private boolean isUsernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

}
