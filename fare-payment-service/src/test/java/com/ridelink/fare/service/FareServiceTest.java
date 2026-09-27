package com.ridelink.fare.service;

import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;
import com.ridelink.fare.model.VehicleType;
import com.ridelink.fare.service.impl.FareServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareServiceTest {

    private FareServiceImpl fareService;

    @BeforeEach
    void setUp() {
        fareService = new FareServiceImpl();
    }

    @Test
    @DisplayName("Should estimate fare based on distance and vehicle base rate")
    void testEstimateFare_CalculatesFormulaCorrectly() {
        // Distance between Colombo Fort (6.9344, 79.8428) and Bambalapitiya (6.8969, 79.8573) is ~4.4 km
        FareEstimateRequest request = new FareEstimateRequest(6.9344, 79.8428, 6.8969, 79.8573, VehicleType.CAR);

        FareEstimateResponse response = fareService.estimateFare(request);

        assertNotNull(response);
        assertEquals(VehicleType.CAR, response.getVehicleType());
        assertTrue(response.getDistanceKm() > 4.0 && response.getDistanceKm() < 5.0);
        assertEquals(150.0, response.getBaseFare());
        assertEquals(95.0, response.getPerKmRate());
        assertTrue(response.getTotalEstimatedFare() > 500.0);
        assertNotNull(response.getCalculationRule());
    }

    @Test
    @DisplayName("Should use lower rates for bike rides")
    void testEstimateFare_BikeLowerRate() {
        FareEstimateRequest request = new FareEstimateRequest(6.9344, 79.8428, 6.8969, 79.8573, VehicleType.BIKE);

        FareEstimateResponse response = fareService.estimateFare(request);

        assertEquals(80.0, response.getBaseFare());
        assertEquals(50.0, response.getPerKmRate());
        assertTrue(response.getTotalEstimatedFare() < 400.0);
    }
}
