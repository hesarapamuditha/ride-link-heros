package com.ridelink.ride.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridelink.ride.dto.RideRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.model.LocationPoint;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.model.VehicleType;
import com.ridelink.ride.security.JwtAuthenticationFilter;
import com.ridelink.ride.security.JwtTokenProvider;
import com.ridelink.ride.service.RideService;
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

@WebMvcTest(RideController.class)
@AutoConfigureMockMvc(addFilters = false)
class RideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RideService rideService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Should return 201 Created on valid ride request")
    void testRequestRide_Success() throws Exception {
        LocationPoint pickup = new LocationPoint("Fort", 6.93, 79.84);
        LocationPoint dest = new LocationPoint("Kollupitiya", 6.90, 79.85);
        RideRequest req = new RideRequest("passenger-1", pickup, dest, VehicleType.CAR);

        RideResponse resp = new RideResponse();
        resp.setId("ride-123");
        resp.setPassengerId("passenger-1");
        resp.setStatus(RideStatus.REQUESTED);
        resp.setEstimatedFare(350.0);

        when(rideService.requestRide(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/rides/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("ride-123"))
                .andExpect(jsonPath("$.data.status").value("REQUESTED"));
    }

    @Test
    @DisplayName("Negative Scenario: Should return 400 Bad Request when passenger ID is blank")
    void testRequestRide_BlankPassenger_Returns400() throws Exception {
        RideRequest req = new RideRequest("", null, null, null);

        mockMvc.perform(post("/api/v1/rides/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.passengerId").exists());
    }
}
