package com.tm7xco.springkeycloakpoc.controller;

import com.tm7xco.springkeycloakpoc.controller.dto.LoginRequest;
import com.tm7xco.springkeycloakpoc.controller.dto.RegisterRequest;
import com.tm7xco.springkeycloakpoc.controller.dto.LoginResponse;
import com.tm7xco.springkeycloakpoc.controller.dto.RegisterResponse;
import com.tm7xco.springkeycloakpoc.domain.AppUser;
import com.tm7xco.springkeycloakpoc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registration) {
        log.info("Processing incoming POST request (/api/user/register) started...");

        AppUser registeredUser = userService.registerUser(registration);
        RegisterResponse response = new RegisterResponse(
                registeredUser == null ? null : registeredUser.getUsername()
        );

        log.info("Processing incoming POST request (/api/user/register) finished!");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest login) {
        log.info("Processing incoming POST request (/api/user/login) started...");

        AppUser authenticatedUser = userService.loginUser(login);

        LoginResponse response;
        HttpStatus responseStatus;
        if (authenticatedUser != null) {
            response = new LoginResponse(authenticatedUser.getUsername());
            responseStatus = HttpStatus.OK;
        } else {
            response = new LoginResponse(null);
            responseStatus = HttpStatus.UNAUTHORIZED;
        }

        log.info("Processing incoming POST request (/api/user/login) finished!");
        return ResponseEntity.status(responseStatus).body(response);
    }

}
