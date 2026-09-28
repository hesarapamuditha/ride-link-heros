package com.ridelink.driver.controller;

import com.ridelink.driver.dto.ApiResponse;
import com.ridelink.driver.dto.VehicleRegistrationRequest;
import com.ridelink.driver.dto.VehicleResponse;
import com.ridelink.driver.model.VehicleStatus;
import com.ridelink.driver.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Vehicle Management", description = "Endpoints for vehicle registration, assignment, and status")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/drivers/{driverId}/vehicles")
    @Operation(summary = "Register vehicle for driver", description = "Registers a new vehicle under a specific driver.")
    public ResponseEntity<ApiResponse<VehicleResponse>> registerVehicle(
            @PathVariable String driverId,
            @Valid @RequestBody VehicleRegistrationRequest request) {
        VehicleResponse response = vehicleService.registerVehicle(driverId, request);
        return new ResponseEntity<>(ApiResponse.ok("Vehicle registered successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/drivers/{driverId}/vehicles")
    @Operation(summary = "Get vehicles of a driver", description = "Lists all vehicles registered by a driver.")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getVehiclesByDriver(@PathVariable String driverId) {
        List<VehicleResponse> vehicles = vehicleService.getVehiclesByDriver(driverId);
        return ResponseEntity.ok(ApiResponse.ok(vehicles));
    }

    @GetMapping("/vehicles/{vehicleId}")
    @Operation(summary = "Get vehicle by ID", description = "Retrieves vehicle details by vehicle ID.")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicleById(@PathVariable String vehicleId) {
        VehicleResponse vehicle = vehicleService.getVehicleById(vehicleId);
        return ResponseEntity.ok(ApiResponse.ok(vehicle));
    }

    @PatchMapping("/drivers/{driverId}/vehicles/{vehicleId}/activate")
    @Operation(summary = "Set active vehicle", description = "Designates a vehicle as the currently active vehicle for the driver.")
    public ResponseEntity<ApiResponse<VehicleResponse>> setVehicleActive(
            @PathVariable String driverId,
            @PathVariable String vehicleId) {
        VehicleResponse updated = vehicleService.setVehicleActive(driverId, vehicleId);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle set as active", updated));
    }

    @PatchMapping("/drivers/{driverId}/vehicles/{vehicleId}/status")
    @Operation(summary = "Update vehicle operational status", description = "Sets vehicle status to ACTIVE, MAINTENANCE, or INACTIVE.")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicleStatus(
            @PathVariable String driverId,
            @PathVariable String vehicleId,
            @RequestParam VehicleStatus status) {
        VehicleResponse updated = vehicleService.updateVehicleStatus(driverId, vehicleId, status);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle status updated to " + status, updated));
    }

    @DeleteMapping("/drivers/{driverId}/vehicles/{vehicleId}")
    @Operation(summary = "Delete vehicle", description = "Removes a vehicle from the driver's profile.")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(
            @PathVariable String driverId,
            @PathVariable String vehicleId) {
        vehicleService.deleteVehicle(driverId, vehicleId);
        return ResponseEntity.ok(ApiResponse.ok("Vehicle deleted successfully", null));
    }
}
