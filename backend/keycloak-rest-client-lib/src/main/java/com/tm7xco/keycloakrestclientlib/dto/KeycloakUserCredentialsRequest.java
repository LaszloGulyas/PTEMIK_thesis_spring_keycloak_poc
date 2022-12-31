package com.tm7xco.keycloakrestclientlib.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonSerialize
public class KeycloakUserCredentialsRequest {

    private String type;
    private String value;
    private Boolean temporary;

}
