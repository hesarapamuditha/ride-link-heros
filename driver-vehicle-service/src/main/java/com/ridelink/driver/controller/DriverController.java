package com.ridelink.driver.controller;

import com.ridelink.driver.dto.ApiResponse;
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
import com.ridelink.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
@Tag(name = "Driver Management", description = "Endpoints for driver operational profiles, availability, location, and matching")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping
    @Operation(summary = "Register driver profile", description = "Creates a new driver operational profile linked to an Account Service user ID.")
    public ResponseEntity<ApiResponse<DriverResponse>> registerDriver(@Valid @RequestBody DriverRegistrationRequest request) {
        DriverResponse response = driverService.registerDriver(request);
        return new ResponseEntity<>(ApiResponse.ok("Driver registered successfully", response), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all drivers", description = "Retrieves all driver profiles (Admin / Operations).")
    public ResponseEntity<ApiResponse<List<DriverResponse>>> getAllDrivers() {
        List<DriverResponse> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(ApiResponse.ok(drivers));
    }

    @GetMapping("/{driverId}")
    @Operation(summary = "Get driver by ID", description = "Retrieves driver profile including active vehicle details.")
    public ResponseEntity<ApiResponse<DriverResponse>> getDriverById(@PathVariable String driverId) {
        DriverResponse driver = driverService.getDriverById(driverId);
        return ResponseEntity.ok(ApiResponse.ok(driver));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get driver by User ID", description = "Retrieves driver profile using Account Service User ID.")
    public ResponseEntity<ApiResponse<DriverResponse>> getDriverByUserId(@PathVariable String userId) {
        DriverResponse driver = driverService.getDriverByUserId(userId);
        return ResponseEntity.ok(ApiResponse.ok(driver));
    }

    @PutMapping("/{driverId}")
    @Operation(summary = "Update driver profile", description = "Updates driver contact details, experience, or operating city.")
    public ResponseEntity<ApiResponse<DriverResponse>> updateDriverProfile(
            @PathVariable String driverId,
            @Valid @RequestBody DriverUpdateRequest request) {
        DriverResponse updated = driverService.updateDriverProfile(driverId, request);
        return ResponseEntity.ok(ApiResponse.ok("Driver profile updated successfully", updated));
    }

    @PatchMapping("/{driverId}/verify")
    @Operation(summary = "Update driver verification status", description = "Admin endpoint to verify, reject, or suspend a driver.")
    public ResponseEntity<ApiResponse<DriverResponse>> updateVerificationStatus(
            @PathVariable String driverId,
            @RequestParam VerificationStatus status) {
        DriverResponse updated = driverService.updateVerificationStatus(driverId, status);
        return ResponseEntity.ok(ApiResponse.ok("Driver verification status updated", updated));
    }

    @PatchMapping("/{driverId}/availability")
    @Operation(summary = "Update driver availability status", description = "Allows driver to set availability (AVAILABLE, BUSY, OFFLINE). Must be VERIFIED with active vehicle to go AVAILABLE.")
    public ResponseEntity<ApiResponse<DriverResponse>> updateAvailabilityStatus(
            @PathVariable String driverId,
            @Valid @RequestBody AvailabilityUpdateRequest request) {
        DriverResponse updated = driverService.updateAvailabilityStatus(driverId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.ok("Availability status updated to " + request.getStatus(), updated));
    }

    @PutMapping("/{driverId}/location")
    @Operation(summary = "Update driver simulated location", description = "Updates driver GPS coordinates (latitude, longitude) and simulated address.")
    public ResponseEntity<ApiResponse<DriverResponse>> updateLocation(
            @PathVariable String driverId,
            @Valid @RequestBody LocationUpdateRequest request) {
        DriverResponse updated = driverService.updateLocation(driverId, request);
        return ResponseEntity.ok(ApiResponse.ok("Driver location updated", updated));
    }

    @PostMapping("/{driverId}/rate")
    @Operation(summary = "Rate a driver", description = "Updates driver average rating score based on rider rating (1.0 to 5.0).")
    public ResponseEntity<ApiResponse<DriverResponse>> rateDriver(
            @PathVariable String driverId,
            @Valid @RequestBody DriverRatingRequest request) {
        DriverResponse updated = driverService.rateDriver(driverId, request);
        return ResponseEntity.ok(ApiResponse.ok("Driver rating updated", updated));
    }

    @GetMapping("/eligible")
    @Operation(summary = "Retrieve eligible available drivers (Interservice)", description = "Core interservice endpoint called by Ride Management Service to find nearest available drivers based on pickup coordinates, vehicle type, and search radius.")
    public ResponseEntity<ApiResponse<List<EligibleDriverResponse>>> findEligibleDrivers(
            @Parameter(description = "Pickup latitude", example = "6.9271") @RequestParam Double pickupLat,
            @Parameter(description = "Pickup longitude", example = "79.8612") @RequestParam Double pickupLng,
            @Parameter(description = "Optional vehicle type filter", example = "CAR") @RequestParam(required = false) VehicleType vehicleType,
            @Parameter(description = "Search radius in km", example = "10.0") @RequestParam(required = false, defaultValue = "10.0") Double radiusKm,
            @Parameter(description = "Maximum results", example = "5") @RequestParam(required = false, defaultValue = "5") Integer limit) {
        List<EligibleDriverResponse> eligible = driverService.findEligibleDrivers(pickupLat, pickupLng, vehicleType, radiusKm, limit);
        return ResponseEntity.ok(ApiResponse.ok(eligible));
    }

    @PatchMapping("/{driverId}/status")
    @Operation(summary = "Internal interservice status update", description = "Called by Ride Management Service to toggle status between BUSY (on assignment) and AVAILABLE (on ride complete/cancel).")
    public ResponseEntity<ApiResponse<DriverResponse>> setInternalDriverStatus(
            @PathVariable String driverId,
            @RequestParam AvailabilityStatus status) {
        DriverResponse updated = driverService.setInternalDriverStatus(driverId, status);
        return ResponseEntity.ok(ApiResponse.ok("Driver status set to " + status, updated));
    }
}
