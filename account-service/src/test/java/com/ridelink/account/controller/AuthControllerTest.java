package com.ridelink.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridelink.account.dto.AuthResponse;
import com.ridelink.account.dto.LoginRequest;
import com.ridelink.account.dto.UserRegistrationRequest;
import com.ridelink.account.model.Role;
import com.ridelink.account.security.JwtAuthenticationFilter;
import com.ridelink.account.security.JwtTokenProvider;
import com.ridelink.account.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Should return 200 OK upon valid login")
    void testLogin_Success_Returns200() throws Exception {
        LoginRequest req = new LoginRequest("user@example.com", "password123");
        AuthResponse auth = new AuthResponse("test.jwt.token", "usr-1", "user@example.com", "User Name", Role.ROLE_PASSENGER, 86400000L);

        when(accountService.login(any())).thenReturn(auth);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("test.jwt.token"));
    }

    @Test
    @DisplayName("Negative Scenario: Should return 400 Bad Request when registering with invalid email format")
    void testRegister_InvalidEmail_Returns400() throws Exception {
        UserRegistrationRequest invalidReq = new UserRegistrationRequest(
                "not-an-email", "secret123", "Alice", "+94771234567", Role.ROLE_PASSENGER
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }
}
