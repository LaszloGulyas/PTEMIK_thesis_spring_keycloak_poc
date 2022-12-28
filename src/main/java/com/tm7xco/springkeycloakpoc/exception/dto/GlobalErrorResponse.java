package com.tm7xco.springkeycloakpoc.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GlobalErrorResponse {

    private final String errorMessage;

}
