package com.tm7xco.springkeycloakpoc.keycloak.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonSerialize
public class KeycloakRealmRoleModel {

    private String id;
    private String name;

}
