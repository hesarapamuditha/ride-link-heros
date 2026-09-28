package com.ridelink.ride.service;

import com.ridelink.ride.dto.EligibleDriverDTO;
import com.ridelink.ride.dto.PaymentProcessResponseDTO;
import com.ridelink.ride.dto.RideRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.exception.InvalidStateTransitionException;
import com.ridelink.ride.exception.NoDriverAvailableException;
import com.ridelink.ride.model.LocationPoint;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.model.VehicleType;
import com.ridelink.ride.repository.RideRepository;
import com.ridelink.ride.service.impl.RideServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private InterserviceClient interserviceClient;

    @InjectMocks
    private RideServiceImpl rideService;

    private Ride sampleRide;

    @BeforeEach
    void setUp() {
        LocationPoint pickup = new LocationPoint("Fort, Colombo", 6.9344, 79.8428);
        LocationPoint dest = new LocationPoint("Bambalapitiya", 6.8969, 79.8573);
        sampleRide = new Ride("passenger-101", pickup, dest, VehicleType.CAR);
        sampleRide.setId("ride-001");
        sampleRide.setEstimatedFare(450.0);
    }

    @Test
    @DisplayName("Should successfully request a new ride and compute fare")
    void testRequestRide_Success() {
        LocationPoint pickup = new LocationPoint("Colombo 01", 6.93, 79.84);
        LocationPoint dest = new LocationPoint("Colombo 04", 6.89, 79.85);
        RideRequest req = new RideRequest("passenger-102", pickup, dest, VehicleType.CAR);

        when(rideRepository.save(any(Ride.class))).thenAnswer(i -> {
            Ride r = i.getArgument(0);
            r.setId("ride-002");
            return r;
        });

        RideResponse response = rideService.requestRide(req);

        assertNotNull(response);
        assertEquals("ride-002", response.getId());
        assertEquals(RideStatus.REQUESTED, response.getStatus());
        assertTrue(response.getEstimatedFare() > 0.0);
    }

    @Test
    @DisplayName("Should assign nearest eligible driver successfully via interservice call")
    void testAssignDriver_Success() {
        sampleRide.setStatus(RideStatus.REQUESTED);
        when(rideRepository.findById("ride-001")).thenReturn(Optional.of(sampleRide));

        EligibleDriverDTO driver = new EligibleDriverDTO();
        driver.setDriverId("driver-500");
        driver.setFullName("Sunil Perera");
        driver.setPhoneNumber("+94770001122");
        driver.setPlateNumber("WP-CA-1234");
        driver.setVehicleMake("Toyota");
        driver.setVehicleModel("Aqua");

        when(interserviceClient.fetchEligibleDrivers(any(), any(), eq(VehicleType.CAR), any(), any()))
                .thenReturn(List.of(driver));
        when(interserviceClient.updateDriverStatus("driver-500", "BUSY")).thenReturn(true);
        when(rideRepository.save(any(Ride.class))).thenAnswer(i -> i.getArgument(0));

        RideResponse response = rideService.assignDriver("ride-001");

        assertNotNull(response);
        assertEquals(RideStatus.ASSIGNED, response.getStatus());
        assertEquals("driver-500", response.getDriverId());
        assertEquals("Sunil Perera", response.getDriverName());
        verify(interserviceClient, times(1)).updateDriverStatus("driver-500", "BUSY");
    }

    @Test
    @DisplayName("Negative Scenario: Should throw NoDriverAvailableException when no drivers in radius")
    void testAssignDriver_NoDriverAvailable_ThrowsException() {
        sampleRide.setStatus(RideStatus.REQUESTED);
        when(rideRepository.findById("ride-001")).thenReturn(Optional.of(sampleRide));
        when(interserviceClient.fetchEligibleDrivers(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(NoDriverAvailableException.class, () -> rideService.assignDriver("ride-001"));
        verify(rideRepository, never()).save(any(Ride.class));
    }

    @Test
    @DisplayName("Negative Scenario: Should throw InvalidStateTransitionException when starting non-accepted ride")
    void testStartRide_InvalidStateTransition_ThrowsException() {
        // Attempting to start a ride that is only in REQUESTED state (not ACCEPTED)
        sampleRide.setStatus(RideStatus.REQUESTED);
        when(rideRepository.findById("ride-001")).thenReturn(Optional.of(sampleRide));

        assertThrows(InvalidStateTransitionException.class, () -> rideService.startRide("ride-001"));
    }

    @Test
    @DisplayName("Should complete ride, release driver to AVAILABLE, and trigger payment")
    void testCompleteRide_Success() {
        sampleRide.setStatus(RideStatus.IN_PROGRESS);
        sampleRide.setDriverId("driver-500");
        when(rideRepository.findById("ride-001")).thenReturn(Optional.of(sampleRide));

        PaymentProcessResponseDTO paymentResp = new PaymentProcessResponseDTO();
        paymentResp.setPaymentId("PAY-999");
        paymentResp.setStatus("COMPLETED");

        when(interserviceClient.processPayment(any())).thenReturn(paymentResp);
        when(rideRepository.save(any(Ride.class))).thenAnswer(i -> i.getArgument(0));

        RideResponse response = rideService.completeRide("ride-001");

        assertEquals(RideStatus.COMPLETED, response.getStatus());
        assertEquals("PAY-999", response.getPaymentId());
        assertEquals("COMPLETED", response.getPaymentStatus());
        verify(interserviceClient, times(1)).updateDriverStatus("driver-500", "AVAILABLE");
    }

    @Test
    @DisplayName("Negative Scenario: Should fail to cancel an already COMPLETED ride")
    void testCancelRide_AlreadyCompleted_ThrowsException() {
        sampleRide.setStatus(RideStatus.COMPLETED);
        when(rideRepository.findById("ride-001")).thenReturn(Optional.of(sampleRide));

        assertThrows(InvalidStateTransitionException.class, () -> rideService.cancelRide("ride-001", "Changed mind"));
    }
}
