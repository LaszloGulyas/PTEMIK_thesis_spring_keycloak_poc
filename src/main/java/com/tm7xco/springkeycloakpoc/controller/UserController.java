package com.tm7xco.springkeycloakpoc.controller;

import com.tm7xco.springkeycloakpoc.controller.dto.RequestLogin;
import com.tm7xco.springkeycloakpoc.controller.dto.RequestRegistration;
import com.tm7xco.springkeycloakpoc.controller.dto.ResponseLogin;
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
    public ResponseEntity<Void> register(@RequestBody RequestRegistration registration) {
        log.info("Processing incoming POST request (/api/user/register) started...");


        log.info("Processing incoming POST request (/api/user/register) finished!");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLogin> login(@RequestBody RequestLogin login) {
        log.info("Processing incoming POST request (/api/user/login) started...");

        ResponseLogin response = null;

        log.info("Processing incoming POST request (/api/user/login) finished!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
