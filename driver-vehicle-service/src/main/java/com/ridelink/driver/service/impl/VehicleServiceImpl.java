package com.ridelink.driver.service.impl;

import com.ridelink.driver.dto.VehicleRegistrationRequest;
import com.ridelink.driver.dto.VehicleResponse;
import com.ridelink.driver.exception.DuplicateResourceException;
import com.ridelink.driver.exception.InvalidOperationException;
import com.ridelink.driver.exception.ResourceNotFoundException;
import com.ridelink.driver.model.AvailabilityStatus;
import com.ridelink.driver.model.Driver;
import com.ridelink.driver.model.Vehicle;
import com.ridelink.driver.model.VehicleStatus;
import com.ridelink.driver.repository.DriverRepository;
import com.ridelink.driver.repository.VehicleRepository;
import com.ridelink.driver.service.VehicleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public VehicleResponse registerVehicle(String driverId, VehicleRegistrationRequest request) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        if (vehicleRepository.existsByPlateNumber(request.getPlateNumber())) {
            throw new DuplicateResourceException("Vehicle with plate number already registered: " + request.getPlateNumber());
        }

        Vehicle vehicle = new Vehicle(
                driverId,
                request.getMake(),
                request.getModel(),
                request.getYear(),
                request.getPlateNumber().toUpperCase().trim(),
                request.getColor(),
                request.getVehicleType(),
                request.getSeatingCapacity()
        );
        vehicle.setCreatedAt(LocalDateTime.now());
        vehicle.setUpdatedAt(LocalDateTime.now());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // If driver does not have an active vehicle yet, set this one as active
        if (driver.getActiveVehicleId() == null) {
            driver.setActiveVehicleId(savedVehicle.getId());
            driver.setUpdatedAt(LocalDateTime.now());
            driverRepository.save(driver);
        }

        return VehicleResponse.fromEntity(savedVehicle);
    }

    @Override
    public List<VehicleResponse> getVehiclesByDriver(String driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new ResourceNotFoundException("Driver not found with ID: " + driverId);
        }
        return vehicleRepository.findByDriverId(driverId).stream()
                .map(VehicleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleResponse getVehicleById(String vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));
        return VehicleResponse.fromEntity(vehicle);
    }

    @Override
    public VehicleResponse setVehicleActive(String driverId, String vehicleId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        Vehicle vehicle = vehicleRepository.findByIdAndDriverId(vehicleId, driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle " + vehicleId + " does not belong to driver " + driverId));

        if (vehicle.getStatus() != VehicleStatus.ACTIVE) {
            throw new InvalidOperationException("Cannot activate vehicle in status: " + vehicle.getStatus());
        }

        driver.setActiveVehicleId(vehicle.getId());
        driver.setUpdatedAt(LocalDateTime.now());
        driverRepository.save(driver);

        return VehicleResponse.fromEntity(vehicle);
    }

    @Override
    public VehicleResponse updateVehicleStatus(String driverId, String vehicleId, VehicleStatus status) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        Vehicle vehicle = vehicleRepository.findByIdAndDriverId(vehicleId, driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found for driver"));

        vehicle.setStatus(status);
        vehicle.setUpdatedAt(LocalDateTime.now());
        Vehicle updated = vehicleRepository.save(vehicle);

        if (status != VehicleStatus.ACTIVE && vehicleId.equals(driver.getActiveVehicleId())) {
            // Find another active vehicle if available
            List<Vehicle> activeVehicles = vehicleRepository.findByDriverIdAndStatus(driverId, VehicleStatus.ACTIVE);
            if (!activeVehicles.isEmpty()) {
                driver.setActiveVehicleId(activeVehicles.get(0).getId());
            } else {
                driver.setActiveVehicleId(null);
                driver.setAvailabilityStatus(AvailabilityStatus.OFFLINE);
            }
            driver.setUpdatedAt(LocalDateTime.now());
            driverRepository.save(driver);
        }

        return VehicleResponse.fromEntity(updated);
    }

    @Override
    public void deleteVehicle(String driverId, String vehicleId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        Vehicle vehicle = vehicleRepository.findByIdAndDriverId(vehicleId, driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found for driver"));

        if (vehicleId.equals(driver.getActiveVehicleId())) {
            driver.setActiveVehicleId(null);
            driver.setAvailabilityStatus(AvailabilityStatus.OFFLINE);
            driver.setUpdatedAt(LocalDateTime.now());
            driverRepository.save(driver);
        }

        vehicleRepository.delete(vehicle);
    }
}
