package com.tm7xco.springrestserver.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class SecurityExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private AuthenticationException authenticationException;
    @Mock
    private AccessDeniedException accessDeniedException;

    private HttpServletResponse response;
    private SecurityExceptionHandler underTest;

    @BeforeEach
    void init() {
        response = new MockHttpServletResponse();
        underTest = new SecurityExceptionHandler();
    }

    @Test
    void testCommence() throws IOException {
        // given
        String expectedContentType = MediaType.APPLICATION_JSON_VALUE;
        int expectedStatus = HttpServletResponse.SC_UNAUTHORIZED;

        // when
        underTest.commence(request, response, authenticationException);

        // then
        assertEquals(expectedContentType, response.getContentType());
        assertEquals(expectedStatus, response.getStatus());
    }

    @Test
    void testHandle() throws IOException {
        // given
        String expectedContentType = MediaType.APPLICATION_JSON_VALUE;
        int expectedStatus = HttpServletResponse.SC_FORBIDDEN;

        // when
        underTest.handle(request, response, accessDeniedException);

        // then
        assertEquals(expectedContentType, response.getContentType());
        assertEquals(expectedStatus, response.getStatus());
    }
}
