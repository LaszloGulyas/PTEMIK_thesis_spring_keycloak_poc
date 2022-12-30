package com.tm7xco.springkeycloakpoc.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakAssignedRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS_MAP_KEY = "realm_access";
    private static final String ROLE_LIST_KEY = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    private static final List<String> availableRoles = Arrays.stream(UserRole.values()).map(UserRole::name).toList();

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, List<String>> realmAccessMap = jwt.getClaim(REALM_ACCESS_MAP_KEY);

        return realmAccessMap.get(ROLE_LIST_KEY).stream()
                .filter(availableRoles::contains)
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .collect(Collectors.toList());
    }

}