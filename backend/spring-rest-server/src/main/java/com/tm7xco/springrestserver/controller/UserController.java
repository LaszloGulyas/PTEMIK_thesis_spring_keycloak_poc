package com.tm7xco.springrestserver.controller;

import com.tm7xco.springrestserver.controller.dto.*;
import com.tm7xco.springrestserver.domain.AppUser;
import com.tm7xco.springrestserver.service.UserService;
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

        RegisterResponse responseBody = new RegisterResponse(registeredUser.getUsername());
        ResponseEntity<RegisterResponse> response = ResponseEntity.status(HttpStatus.CREATED).body(responseBody);

        log.info("Processing incoming POST request (/api/user/register) finished!");
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest login) {
        log.info("Processing incoming POST request (/api/user/login) started...");

        LoginResponse response = userService.loginUser(login);

        log.info("Processing incoming POST request (/api/user/login) finished!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        log.info("Processing incoming DELETE request (/api/user/) started...");

        userService.deleteUser();

        log.info("Processing incoming DELETE request (/api/user/) finished!");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        log.info("Processing incoming PUT request (/api/user/update-password) started...");

        userService.updateUserPassword(updatePasswordRequest);

        log.info("Processing incoming PUT request (/api/user/update-password) finished!");
        return ResponseEntity.ok().build();
    }

}
