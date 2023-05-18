package com.tm7xco.springrestserver.controller;

import com.tm7xco.springrestserver.controller.dto.BusinessResponse;
import com.tm7xco.springrestserver.service.BusinessService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BusinessController.class)
class BusinessControllerTest extends AbstractControllerTest {

    private static final BusinessResponse TEST_BUSINESS_RESPONSE = new BusinessResponse("test response");

    @MockBean
    private BusinessService businessService;


    @Test
    @WithMockUser(username = TEST_USERNAME, password = TEST_PASSWORD, roles = TEST_ROLE)
    void testExecuteUserContent() throws Exception {
        when(businessService.restrictedBusinessFunction()).thenReturn(TEST_BUSINESS_RESPONSE);

        mockMvc.perform(get("/api/business/user/execute"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"content\":\"test response\"}")));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME, password = TEST_PASSWORD, roles = TEST_ROLE)
    void testExecuteSuperUserContent() throws Exception {
        when(businessService.restrictedBusinessFunction()).thenReturn(TEST_BUSINESS_RESPONSE);

        mockMvc.perform(get("/api/business/super-user/execute"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"content\":\"test response\"}")));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME, password = TEST_PASSWORD, roles = TEST_ROLE)
    void testExecuteAdminContent() throws Exception {
        when(businessService.restrictedBusinessFunction()).thenReturn(TEST_BUSINESS_RESPONSE);

        mockMvc.perform(get("/api/business/admin/execute"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"content\":\"test response\"}")));
    }

}
