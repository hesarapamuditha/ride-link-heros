package com.ridelink.driver.service;

import com.ridelink.driver.dto.VehicleRegistrationRequest;
import com.ridelink.driver.dto.VehicleResponse;
import com.ridelink.driver.model.VehicleStatus;

import java.util.List;

public interface VehicleService {

    VehicleResponse registerVehicle(String driverId, VehicleRegistrationRequest request);

    List<VehicleResponse> getVehiclesByDriver(String driverId);

    VehicleResponse getVehicleById(String vehicleId);

    VehicleResponse setVehicleActive(String driverId, String vehicleId);

    VehicleResponse updateVehicleStatus(String driverId, String vehicleId, VehicleStatus status);

    void deleteVehicle(String driverId, String vehicleId);
}
