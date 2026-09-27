package com.ridelink.driver.service;

import com.ridelink.driver.dto.DriverRatingRequest;
import com.ridelink.driver.dto.DriverRegistrationRequest;
import com.ridelink.driver.dto.DriverResponse;
import com.ridelink.driver.dto.EligibleDriverResponse;
import com.ridelink.driver.exception.DuplicateResourceException;
import com.ridelink.driver.exception.InvalidOperationException;
import com.ridelink.driver.model.AvailabilityStatus;
import com.ridelink.driver.model.Driver;
import com.ridelink.driver.model.Location;
import com.ridelink.driver.model.Vehicle;
import com.ridelink.driver.model.VehicleStatus;
import com.ridelink.driver.model.VehicleType;
import com.ridelink.driver.model.VerificationStatus;
import com.ridelink.driver.repository.DriverRepository;
import com.ridelink.driver.repository.VehicleRepository;
import com.ridelink.driver.service.impl.DriverServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver sampleDriver;

    @BeforeEach
    void setUp() {
        sampleDriver = new Driver(
                "user-101",
                "Kasun Perera",
                "kasun@example.com",
                "+94771234567",
                "DL-998877",
                5,
                "Colombo"
        );
        sampleDriver.setId("driver-001");
        sampleDriver.setVerificationStatus(VerificationStatus.VERIFIED);
        sampleDriver.setActiveVehicleId("vehicle-001");
        sampleDriver.setCurrentLocation(new Location(6.9271, 79.8612, "Fort, Colombo"));
        sampleDriver.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Should successfully register a new driver")
    void testRegisterDriver_Success() {
        DriverRegistrationRequest request = new DriverRegistrationRequest(
                "user-102",
                "Nimal Silva",
                "nimal@example.com",
                "+94779876543",
                "DL-112233",
                3,
                "Kandy"
        );

        when(driverRepository.existsByUserId("user-102")).thenReturn(false);
        when(driverRepository.existsByEmail("nimal@example.com")).thenReturn(false);
        when(driverRepository.existsByLicenseNumber("DL-112233")).thenReturn(false);
        when(driverRepository.save(any(Driver.class))).thenAnswer(invocation -> {
            Driver d = invocation.getArgument(0);
            d.setId("driver-002");
            return d;
        });

        DriverResponse response = driverService.registerDriver(request);

        assertNotNull(response);
        assertEquals("driver-002", response.getId());
        assertEquals("Nimal Silva", response.getFullName());
        assertEquals(VerificationStatus.PENDING_VERIFICATION, response.getVerificationStatus());
        assertEquals(AvailabilityStatus.OFFLINE, response.getAvailabilityStatus());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    @DisplayName("Negative Scenario: Should throw DuplicateResourceException on duplicate email")
    void testRegisterDriver_DuplicateEmail_ThrowsException() {
        DriverRegistrationRequest request = new DriverRegistrationRequest(
                "user-103",
                "Duplicate Driver",
                "kasun@example.com",
                "+94771234567",
                "DL-555555",
                4,
                "Colombo"
        );

        when(driverRepository.existsByUserId("user-103")).thenReturn(false);
        when(driverRepository.existsByEmail("kasun@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> driverService.registerDriver(request));
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    @DisplayName("Negative Scenario: Should fail when driver goes AVAILABLE without VERIFIED status")
    void testUpdateAvailability_NotVerified_ThrowsException() {
        sampleDriver.setVerificationStatus(VerificationStatus.PENDING_VERIFICATION);
        when(driverRepository.findById("driver-001")).thenReturn(Optional.of(sampleDriver));

        assertThrows(InvalidOperationException.class, () ->
                driverService.updateAvailabilityStatus("driver-001", AvailabilityStatus.AVAILABLE)
        );
        verify(driverRepository, never()).save(sampleDriver);
    }

    @Test
    @DisplayName("Negative Scenario: Should fail when driver goes AVAILABLE without active vehicle")
    void testUpdateAvailability_NoActiveVehicle_ThrowsException() {
        sampleDriver.setActiveVehicleId(null);
        when(driverRepository.findById("driver-001")).thenReturn(Optional.of(sampleDriver));

        assertThrows(InvalidOperationException.class, () ->
                driverService.updateAvailabilityStatus("driver-001", AvailabilityStatus.AVAILABLE)
        );
    }

    @Test
    @DisplayName("Should find eligible drivers within radius and sort nearest first")
    void testFindEligibleDrivers_WithinRadius_ReturnsSortedList() {
        Vehicle vehicle = new Vehicle("driver-001", "Toyota", "Prius", 2020, "CAB-5566", "White", VehicleType.CAR, 4);
        vehicle.setId("vehicle-001");
        vehicle.setStatus(VehicleStatus.ACTIVE);

        when(driverRepository.findByAvailabilityStatusAndVerificationStatus(
                AvailabilityStatus.AVAILABLE, VerificationStatus.VERIFIED))
                .thenReturn(List.of(sampleDriver));
        when(vehicleRepository.findById("vehicle-001")).thenReturn(Optional.of(vehicle));

        // Pickup at Galle Face, Colombo (approx 1.5 km away)
        List<EligibleDriverResponse> eligible = driverService.findEligibleDrivers(
                6.9200, 79.8450, VehicleType.CAR, 10.0, 5
        );

        assertNotNull(eligible);
        assertEquals(1, eligible.size());
        assertEquals("driver-001", eligible.get(0).getDriverId());
        assertTrue(eligible.get(0).getDistanceKm() > 0.0);
        assertTrue(eligible.get(0).getDistanceKm() <= 10.0);
        assertEquals("CAB-5566", eligible.get(0).getPlateNumber());
    }

    @Test
    @DisplayName("Should update driver rating correctly with cumulative average")
    void testRateDriver_CalculatesCorrectAverage() {
        sampleDriver.setRating(4.0);
        sampleDriver.setTotalRatings(1);
        when(driverRepository.findById("driver-001")).thenReturn(Optional.of(sampleDriver));
        when(driverRepository.save(any(Driver.class))).thenAnswer(i -> i.getArgument(0));

        // New rating is 5.0 -> new average should be (4.0*1 + 5.0)/2 = 4.5
        DriverResponse response = driverService.rateDriver("driver-001", new DriverRatingRequest(5.0, "Great trip!"));

        assertEquals(4.5, response.getRating());
        assertEquals(2, response.getTotalRatings());
    }
}
