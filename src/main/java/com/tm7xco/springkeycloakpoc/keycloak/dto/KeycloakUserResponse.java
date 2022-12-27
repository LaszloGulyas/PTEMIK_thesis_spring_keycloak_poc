package com.tm7xco.springkeycloakpoc.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakUserResponse {

    private String username;

}
