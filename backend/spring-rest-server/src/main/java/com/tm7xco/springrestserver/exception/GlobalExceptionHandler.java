package com.tm7xco.springrestserver.exception;

import com.tm7xco.springrestserver.exception.dto.GlobalErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE_500 = "Unknown server error.";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalErrorResponse> handleException(Exception e) {
        log.error(e.getMessage());
        return createErrorResponse(DEFAULT_ERROR_MESSAGE_500, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<GlobalErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        log.error(e.getMessage());
        return createErrorResponse(e.getReason(), e.getStatusCode());
    }

    private ResponseEntity<GlobalErrorResponse> createErrorResponse(String message, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode).body(new GlobalErrorResponse(message));
    }

}
