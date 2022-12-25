package com.tm7xco.springkeycloakpoc.controller;

import com.tm7xco.springkeycloakpoc.controller.dto.*;
import com.tm7xco.springkeycloakpoc.domain.AppUser;
import com.tm7xco.springkeycloakpoc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

        LoginResponse response = userService.loginUser(login);
        HttpStatus responseStatus = response == null ? HttpStatus.UNAUTHORIZED : HttpStatus.OK;

        log.info("Processing incoming POST request (/api/user/login) finished!");
        return ResponseEntity.status(responseStatus).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        log.info("Processing incoming DELETE request (/api/user/) started...");

        boolean isUserDeleted = userService.deleteUser();

        ResponseEntity<Void> response;
        if (isUserDeleted) {
            response = ResponseEntity.ok().build();
        } else {
            response = ResponseEntity.notFound().build();
        }

        log.info("Processing incoming DELETE request (/api/user/) finished!");
        return response;
    }

    @PutMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        log.info("Processing incoming PUT request (/api/user/update-password) started...");

        boolean isPasswordUpdated = userService.updateUserPassword(updatePasswordRequest);

        ResponseEntity<Void> response;
        if (isPasswordUpdated) {
            response = ResponseEntity.ok().build();
        } else {
            response = ResponseEntity.internalServerError().build();
        }

        log.info("Processing incoming PUT request (/api/user/update-password) finished!");
        return response;
    }

}
