package com.tm7xco.springkeycloakpoc.keycloak.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakTokenResponse {

    private String accessToken;
    private Integer expiresIn;
    private Integer refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
    private String sessionState;
    private String scope;

}
