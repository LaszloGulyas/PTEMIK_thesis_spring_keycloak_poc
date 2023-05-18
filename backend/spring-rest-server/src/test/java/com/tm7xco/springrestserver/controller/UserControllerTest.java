package com.tm7xco.springrestserver.controller;

import com.tm7xco.springrestserver.controller.dto.LoginRequest;
import com.tm7xco.springrestserver.controller.dto.LoginResponse;
import com.tm7xco.springrestserver.controller.dto.RegisterRequest;
import com.tm7xco.springrestserver.controller.dto.UpdatePasswordRequest;
import com.tm7xco.springrestserver.domain.AppUser;
import com.tm7xco.springrestserver.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest extends AbstractControllerTest {

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_TOKEN = "bearer token";

    private static final AppUser TEST_APP_USER = new AppUser(1L, TEST_USERNAME, TEST_EMAIL);
    private static final LoginResponse TEST_LOGIN_RESPONSE = new LoginResponse(TEST_USERNAME, TEST_TOKEN);

    @MockBean
    private UserService userService;


    @Test
    @WithMockUser(username = TEST_USERNAME, password = TEST_PASSWORD, roles = TEST_ROLE)
    void testRegister() throws Exception {
        String requestBody = "{\"username\":\"" + TEST_USERNAME
                + "\",\"password\":\"" + TEST_PASSWORD
                + "\",\"email\":\"" + TEST_EMAIL + "\"}";
        String responseBody = "{\"username\":\"" + TEST_USERNAME + "\"}";

        when(userService.registerUser(any(RegisterRequest.class))).thenReturn(TEST_APP_USER);

        mockMvc.perform(post("/api/user/register")
                        .with(csrf())
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().string(responseBody));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME, password = TEST_PASSWORD, roles = TEST_ROLE)
    void testLogin() throws Exception {
        String requestBody = "{\"username\":\"" + TEST_USERNAME + "\",\"password\":\"" + TEST_PASSWORD + "\"}";
        String responseBody = "{\"username\":\"" + TEST_USERNAME + "\",\"token\":\"" + TEST_TOKEN + "\"}";

        when(userService.loginUser(any(LoginRequest.class))).thenReturn(TEST_LOGIN_RESPONSE);

        mockMvc.perform(post("/api/user/login")
                        .with(csrf())
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(responseBody));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME, password = TEST_PASSWORD, roles = TEST_ROLE)
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser();

        mockMvc.perform(delete("/api/user")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = TEST_USERNAME, password = TEST_PASSWORD, roles = TEST_ROLE)
    void testUpdatePassword() throws Exception {
        String requestBody = "{\"password\":\"" + TEST_PASSWORD + "\"}";

        doNothing().when(userService).updateUserPassword(any(UpdatePasswordRequest.class));

        mockMvc.perform(put("/api/user/update-password")
                        .with(csrf())
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk());
    }

}
