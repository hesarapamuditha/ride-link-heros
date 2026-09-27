package com.ridelink.ride.controller;

import com.ridelink.ride.dto.ApiResponse;
import com.ridelink.ride.dto.CancelRideRequest;
import com.ridelink.ride.dto.RideRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
@Tag(name = "Ride Lifecycle Management", description = "Endpoints for ride requests, driver assignment, state transitions, and cancellation")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/request")
    @Operation(summary = "Request a ride", description = "Creates a new ride request with pickup, destination, and vehicle type. Computes distance and initial fare estimate.")
    public ResponseEntity<ApiResponse<RideResponse>> requestRide(@Valid @RequestBody RideRequest request) {
        RideResponse response = rideService.requestRide(request);
        return new ResponseEntity<>(ApiResponse.ok("Ride requested successfully", response), HttpStatus.CREATED);
    }

    @PostMapping("/{rideId}/assign-driver")
    @Operation(summary = "Assign driver (Interservice)", description = "Queries Driver & Vehicle Service to find nearest eligible available driver and assigns to ride.")
    public ResponseEntity<ApiResponse<RideResponse>> assignDriver(@PathVariable String rideId) {
        RideResponse response = rideService.assignDriver(rideId);
        return ResponseEntity.ok(ApiResponse.ok("Driver assigned to ride", response));
    }

    @PutMapping("/{rideId}/accept")
    @Operation(summary = "Accept ride", description = "Driver accepts an assigned ride. Transitions status to ACCEPTED.")
    public ResponseEntity<ApiResponse<RideResponse>> acceptRide(@PathVariable String rideId) {
        RideResponse response = rideService.acceptRide(rideId);
        return ResponseEntity.ok(ApiResponse.ok("Ride accepted by driver", response));
    }

    @PutMapping("/{rideId}/start")
    @Operation(summary = "Start ride", description = "Driver starts the trip upon passenger pickup. Transitions status to IN_PROGRESS.")
    public ResponseEntity<ApiResponse<RideResponse>> startRide(@PathVariable String rideId) {
        RideResponse response = rideService.startRide(rideId);
        return ResponseEntity.ok(ApiResponse.ok("Ride trip started", response));
    }

    @PutMapping("/{rideId}/complete")
    @Operation(summary = "Complete ride", description = "Driver completes the trip. Sets driver back to AVAILABLE and invokes Fare & Payment Service.")
    public ResponseEntity<ApiResponse<RideResponse>> completeRide(@PathVariable String rideId) {
        RideResponse response = rideService.completeRide(rideId);
        return ResponseEntity.ok(ApiResponse.ok("Ride completed successfully", response));
    }

    @PutMapping("/{rideId}/cancel")
    @Operation(summary = "Cancel ride", description = "Passenger or driver cancels ride. Releases assigned driver and transitions to CANCELLED.")
    public ResponseEntity<ApiResponse<RideResponse>> cancelRide(
            @PathVariable String rideId,
            @RequestBody(required = false) CancelRideRequest request) {
        String reason = (request != null && request.getReason() != null) ? request.getReason() : "Cancelled by user";
        RideResponse response = rideService.cancelRide(rideId, reason);
        return ResponseEntity.ok(ApiResponse.ok("Ride cancelled", response));
    }

    @GetMapping("/{rideId}")
    @Operation(summary = "Get ride by ID", description = "Retrieves full ride record and lifecycle details.")
    public ResponseEntity<ApiResponse<RideResponse>> getRideById(@PathVariable String rideId) {
        RideResponse response = rideService.getRideById(rideId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/passenger/{passengerId}")
    @Operation(summary = "Get rides by passenger", description = "Lists all rides requested by a given passenger.")
    public ResponseEntity<ApiResponse<List<RideResponse>>> getRidesByPassenger(@PathVariable String passengerId) {
        List<RideResponse> rides = rideService.getRidesByPassenger(passengerId);
        return ResponseEntity.ok(ApiResponse.ok(rides));
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "Get rides by driver", description = "Lists all rides accepted or completed by a given driver.")
    public ResponseEntity<ApiResponse<List<RideResponse>>> getRidesByDriver(@PathVariable String driverId) {
        List<RideResponse> rides = rideService.getRidesByDriver(driverId);
        return ResponseEntity.ok(ApiResponse.ok(rides));
    }

    @GetMapping
    @Operation(summary = "Get all rides", description = "Lists all rides across the platform (Admin / Operations).")
    public ResponseEntity<ApiResponse<List<RideResponse>>> getAllRides() {
        List<RideResponse> rides = rideService.getAllRides();
        return ResponseEntity.ok(ApiResponse.ok(rides));
    }
}
