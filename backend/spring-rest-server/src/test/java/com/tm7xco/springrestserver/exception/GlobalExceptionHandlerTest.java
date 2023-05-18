package com.tm7xco.springrestserver.exception;

import com.tm7xco.springrestserver.exception.dto.GlobalErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static final String TEST_MESSAGE = "test exception happened";

    private GlobalExceptionHandler underTest;

    @BeforeEach
    void init() {
        underTest = new GlobalExceptionHandler();
    }

    @Test
    void testHandleException() {
        // given
        Exception testException = new Exception(TEST_MESSAGE);

        // when
        ResponseEntity<GlobalErrorResponse> result = underTest.handleException(testException);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Unknown server error.", result.getBody().getErrorMessage());
    }

    @Test
    void testHandleResponseStatusException() {
        // given
        ResponseStatusException testException = new ResponseStatusException(HttpStatus.BAD_REQUEST, TEST_MESSAGE);

        // when
        ResponseEntity<GlobalErrorResponse> result = underTest.handleResponseStatusException(testException);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(TEST_MESSAGE, result.getBody().getErrorMessage());
    }

}
