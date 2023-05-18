package com.tm7xco.springrestserver.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class KeycloakAssignedRolesConverterTest {

    private static final String REALM_ACCESS_MAP_KEY = "realm_access";
    private static final String ROLE_LIST_KEY = "roles";

    private static final String VALID_ROLE_1 = UserRole.ADMIN.name();
    private static final String VALID_ROLE_2 = UserRole.USER.name();
    private static final String INVALID_ROLE = "invalid-role";


    private KeycloakAssignedRolesConverter underTest;

    @BeforeEach
    void init() {
        underTest = new KeycloakAssignedRolesConverter();
    }

    private static Stream<Arguments> provideRoleCombinations() {
        return Stream.of(
                Arguments.of(List.of(VALID_ROLE_1), List.of()),
                Arguments.of(List.of(VALID_ROLE_1, VALID_ROLE_2), List.of()),
                Arguments.of(List.of(VALID_ROLE_1, VALID_ROLE_2), List.of(INVALID_ROLE)),
                Arguments.of(List.of(), List.of(INVALID_ROLE))
        );
    }

    @ParameterizedTest
    @MethodSource("provideRoleCombinations")
    void testConvert(List<String> validRoles, List<String> invalidRoles) {
        // given
        int expectedCountOfAuthorities = validRoles.size();
        List<String> testRoles = Stream.concat(validRoles.stream(), invalidRoles.stream()).toList();
        Map<String, List<String>> testAccessMap = Map.of(ROLE_LIST_KEY, testRoles);

        Jwt testJwt = Jwt
                .withTokenValue("dummy")
                .header("dummy", "dummy")
                .claim(REALM_ACCESS_MAP_KEY, testAccessMap)
                .build();

        // when
        Collection<GrantedAuthority> result = underTest.convert(testJwt);

        // then
        assertEquals(expectedCountOfAuthorities, result.size());
    }

}
