package com.tm7xco.springkeycloakpoc.keycloak.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonSerialize
public class KeycloakUserCredentialsModel {

    private String type;
    private String value;
    private Boolean temporary;

}
