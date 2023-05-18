package com.tm7xco.springrestserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
abstract class AbstractControllerTest {

    protected static final String TEST_USERNAME = "testUsername";
    protected static final String TEST_PASSWORD = "testPassword";
    protected static final String TEST_ROLE = "USER";

    @Autowired
    protected MockMvc mockMvc;
}
