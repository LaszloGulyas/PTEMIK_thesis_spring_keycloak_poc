package com.tm7xco.springrestserver.service;


import com.tm7xco.springrestserver.controller.dto.BusinessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessServiceTest {

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    private BusinessService underTest;

    @BeforeEach
    void init() {
        underTest = new BusinessService();
    }

    @Test
    void testRestrictedBusinessFunction() {
        // given
        String testRole1 = "ROLE_1";
        String testRole2 = "ROLE_2";
        mockSecurityContext(testRole1, testRole2);

        // when
        BusinessResponse result = underTest.restrictedBusinessFunction();

        // then
        assertTrue(result.getContent().contains(testRole1));
        assertTrue(result.getContent().contains(testRole2));
    }

    private void mockSecurityContext(String... roles) {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        doReturn(AuthorityUtils.createAuthorityList(roles)).when(authentication).getAuthorities();
    }

}
