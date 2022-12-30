package com.tm7xco.springkeycloakpoc.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class LoginResponse {

    private final String username;
    private final String token;

}
