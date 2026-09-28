package com.ridelink.driver.service;

import com.ridelink.driver.dto.VehicleRegistrationRequest;
import com.ridelink.driver.dto.VehicleResponse;
import com.ridelink.driver.exception.DuplicateResourceException;
import com.ridelink.driver.model.Driver;
import com.ridelink.driver.model.Vehicle;
import com.ridelink.driver.model.VehicleStatus;
import com.ridelink.driver.model.VehicleType;
import com.ridelink.driver.repository.DriverRepository;
import com.ridelink.driver.repository.VehicleRepository;
import com.ridelink.driver.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Driver driver;

    @BeforeEach
    void setUp() {
        driver = new Driver("user-1", "John Doe", "john@example.com", "+94770000000", "LIC-11", 5, "Colombo");
        driver.setId("drv-123");
    }

    @Test
    @DisplayName("Should register vehicle and auto-set as active vehicle if driver has none")
    void testRegisterVehicle_Success() {
        VehicleRegistrationRequest req = new VehicleRegistrationRequest(
                "Honda", "Civic", 2022, "WP-CAA-1122", "Black", VehicleType.CAR, 4
        );

        when(driverRepository.findById("drv-123")).thenReturn(Optional.of(driver));
        when(vehicleRepository.existsByPlateNumber("WP-CAA-1122")).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(i -> {
            Vehicle v = i.getArgument(0);
            v.setId("veh-999");
            return v;
        });

        VehicleResponse res = vehicleService.registerVehicle("drv-123", req);

        assertNotNull(res);
        assertEquals("veh-999", res.getId());
        assertEquals("WP-CAA-1122", res.getPlateNumber());
        assertEquals("veh-999", driver.getActiveVehicleId());
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    @DisplayName("Negative Scenario: Should reject vehicle registration when plate number exists")
    void testRegisterVehicle_DuplicatePlate_ThrowsException() {
        VehicleRegistrationRequest req = new VehicleRegistrationRequest(
                "Toyota", "Axio", 2018, "WP-CAA-1122", "White", VehicleType.CAR, 4
        );

        when(driverRepository.findById("drv-123")).thenReturn(Optional.of(driver));
        when(vehicleRepository.existsByPlateNumber("WP-CAA-1122")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> vehicleService.registerVehicle("drv-123", req));
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should activate a vehicle successfully")
    void testSetVehicleActive_Success() {
        Vehicle v = new Vehicle("drv-123", "Toyota", "Axio", 2018, "WP-CAA-9999", "White", VehicleType.CAR, 4);
        v.setId("veh-888");
        v.setStatus(VehicleStatus.ACTIVE);

        when(driverRepository.findById("drv-123")).thenReturn(Optional.of(driver));
        when(vehicleRepository.findByIdAndDriverId("veh-888", "drv-123")).thenReturn(Optional.of(v));

        VehicleResponse res = vehicleService.setVehicleActive("drv-123", "veh-888");

        assertEquals("veh-888", res.getId());
        assertEquals("veh-888", driver.getActiveVehicleId());
        verify(driverRepository, times(1)).save(driver);
    }
}
