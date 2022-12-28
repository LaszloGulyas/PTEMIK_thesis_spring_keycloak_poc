package com.tm7xco.springkeycloakpoc.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm7xco.springkeycloakpoc.exception.dto.GlobalErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private static final String ERROR_MESSAGE_401 = "Authentication failed.";
    private static final String ERROR_MESSAGE_403 = "No access to the called endpoint.";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        updateResponseWithExceptionDetails(ERROR_MESSAGE_401, HttpServletResponse.SC_UNAUTHORIZED, response);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        updateResponseWithExceptionDetails(ERROR_MESSAGE_403, HttpServletResponse.SC_FORBIDDEN, response);
    }

    private void updateResponseWithExceptionDetails(String errorMessage, int statusCode,  HttpServletResponse response) throws IOException {
        GlobalErrorResponse errorResponse = new GlobalErrorResponse(errorMessage);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(statusCode);

        OutputStream responseStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, errorResponse);
        responseStream.flush();
    }

}
