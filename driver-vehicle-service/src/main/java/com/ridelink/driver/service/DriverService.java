package com.ridelink.driver.service;

import com.ridelink.driver.dto.AvailabilityUpdateRequest;
import com.ridelink.driver.dto.DriverRatingRequest;
import com.ridelink.driver.dto.DriverRegistrationRequest;
import com.ridelink.driver.dto.DriverResponse;
import com.ridelink.driver.dto.DriverUpdateRequest;
import com.ridelink.driver.dto.EligibleDriverResponse;
import com.ridelink.driver.dto.LocationUpdateRequest;
import com.ridelink.driver.model.AvailabilityStatus;
import com.ridelink.driver.model.VehicleType;
import com.ridelink.driver.model.VerificationStatus;

import java.util.List;

public interface DriverService {

    DriverResponse registerDriver(DriverRegistrationRequest request);

    DriverResponse getDriverById(String driverId);

    DriverResponse getDriverByUserId(String userId);

    DriverResponse updateDriverProfile(String driverId, DriverUpdateRequest request);

    DriverResponse updateVerificationStatus(String driverId, VerificationStatus status);

    DriverResponse updateAvailabilityStatus(String driverId, AvailabilityStatus status);

    DriverResponse updateLocation(String driverId, LocationUpdateRequest request);

    DriverResponse rateDriver(String driverId, DriverRatingRequest request);

    List<EligibleDriverResponse> findEligibleDrivers(Double pickupLat, Double pickupLng, VehicleType vehicleType, Double radiusKm, Integer limit);

    DriverResponse setInternalDriverStatus(String driverId, AvailabilityStatus status);

    List<DriverResponse> getAllDrivers();
}
