package com.ridelink.driver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridelink.driver.dto.DriverRegistrationRequest;
import com.ridelink.driver.dto.DriverResponse;
import com.ridelink.driver.dto.EligibleDriverResponse;
import com.ridelink.driver.model.AvailabilityStatus;
import com.ridelink.driver.model.VehicleType;
import com.ridelink.driver.model.VerificationStatus;
import com.ridelink.driver.security.JwtAuthenticationFilter;
import com.ridelink.driver.security.JwtTokenProvider;
import com.ridelink.driver.service.DriverService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DriverController.class)
@AutoConfigureMockMvc(addFilters = false)
class DriverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DriverService driverService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("Negative Scenario: Should return 400 Bad Request when validation fails on blank fields")
    void testRegisterDriver_ValidationFailure_Returns400() throws Exception {
        DriverRegistrationRequest invalidReq = new DriverRegistrationRequest();
        // All fields blank

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    @DisplayName("Should return 201 Created when registering a valid driver")
    void testRegisterDriver_ValidRequest_Returns201() throws Exception {
        DriverRegistrationRequest req = new DriverRegistrationRequest(
                "usr-1", "Saman Kumara", "saman@gmail.com", "+94712345678", "DL-99999", 5, "Colombo"
        );

        DriverResponse resp = new DriverResponse();
        resp.setId("drv-1");
        resp.setFullName("Saman Kumara");
        resp.setEmail("saman@gmail.com");
        resp.setVerificationStatus(VerificationStatus.PENDING_VERIFICATION);
        resp.setAvailabilityStatus(AvailabilityStatus.OFFLINE);

        when(driverService.registerDriver(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("drv-1"))
                .andExpect(jsonPath("$.data.fullName").value("Saman Kumara"));
    }

    @Test
    @DisplayName("Should return 200 OK when querying eligible drivers")
    void testFindEligibleDrivers_Returns200() throws Exception {
        EligibleDriverResponse ed = new EligibleDriverResponse(
                "drv-1", "usr-1", "Saman", "+94712345678", 4.9, 20,
                "veh-1", "Toyota", "Prius", "CAB-1122", VehicleType.CAR, "Black",
                6.9271, 79.8612, 1.8, 4
        );

        when(driverService.findEligibleDrivers(6.9200, 79.8600, VehicleType.CAR, 10.0, 5))
                .thenReturn(List.of(ed));

        mockMvc.perform(get("/api/v1/drivers/eligible")
                        .param("pickupLat", "6.9200")
                        .param("pickupLng", "79.8600")
                        .param("vehicleType", "CAR")
                        .param("radiusKm", "10.0")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].driverId").value("drv-1"))
                .andExpect(jsonPath("$.data[0].distanceKm").value(1.8));
    }
}
