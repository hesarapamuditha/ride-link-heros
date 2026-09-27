package com.ridelink.fare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridelink.fare.dto.PaymentProcessRequest;
import com.ridelink.fare.dto.PaymentResponse;
import com.ridelink.fare.model.PaymentMethod;
import com.ridelink.fare.model.PaymentStatus;
import com.ridelink.fare.security.JwtAuthenticationFilter;
import com.ridelink.fare.security.JwtTokenProvider;
import com.ridelink.fare.service.FareService;
import com.ridelink.fare.service.PaymentService;
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

@WebMvcTest({PaymentController.class, FareController.class})
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private FareService fareService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Should return 201 Created upon successful simulated payment")
    void testProcessPayment_Success() throws Exception {
        PaymentProcessRequest req = new PaymentProcessRequest(
                "ride-100", "pass-1", "driver-1", 450.0, PaymentMethod.CREDIT_CARD
        );

        PaymentResponse resp = new PaymentResponse();
        resp.setPaymentId("PAY-555");
        resp.setRideId("ride-100");
        resp.setAmount(450.0);
        resp.setStatus(PaymentStatus.COMPLETED);

        when(paymentService.processPayment(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/payments/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.paymentId").value("PAY-555"))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));
    }

    @Test
    @DisplayName("Negative Scenario: Should return 400 Bad Request when amount is negative or zero")
    void testProcessPayment_InvalidAmount_Returns400() throws Exception {
        PaymentProcessRequest req = new PaymentProcessRequest(
                "ride-100", "pass-1", "driver-1", -50.0, PaymentMethod.CREDIT_CARD
        );

        mockMvc.perform(post("/api/v1/payments/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.amount").exists());
    }
}
