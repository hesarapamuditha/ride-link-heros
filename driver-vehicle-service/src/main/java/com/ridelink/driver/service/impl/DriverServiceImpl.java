package com.ridelink.driver.service.impl;

import com.ridelink.driver.dto.DriverRatingRequest;
import com.ridelink.driver.dto.DriverRegistrationRequest;
import com.ridelink.driver.dto.DriverResponse;
import com.ridelink.driver.dto.DriverUpdateRequest;
import com.ridelink.driver.dto.EligibleDriverResponse;
import com.ridelink.driver.dto.LocationUpdateRequest;
import com.ridelink.driver.dto.VehicleResponse;
import com.ridelink.driver.exception.DuplicateResourceException;
import com.ridelink.driver.exception.InvalidOperationException;
import com.ridelink.driver.exception.ResourceNotFoundException;
import com.ridelink.driver.model.AvailabilityStatus;
import com.ridelink.driver.model.Driver;
import com.ridelink.driver.model.Location;
import com.ridelink.driver.model.Vehicle;
import com.ridelink.driver.model.VehicleStatus;
import com.ridelink.driver.model.VehicleType;
import com.ridelink.driver.model.VerificationStatus;
import com.ridelink.driver.repository.DriverRepository;
import com.ridelink.driver.repository.VehicleRepository;
import com.ridelink.driver.service.DriverService;
import com.ridelink.driver.util.DistanceCalculator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public DriverServiceImpl(DriverRepository driverRepository, VehicleRepository vehicleRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public DriverResponse registerDriver(DriverRegistrationRequest request) {
        if (driverRepository.existsByUserId(request.getUserId())) {
            throw new DuplicateResourceException("A driver profile already exists for User ID: " + request.getUserId());
        }
        if (driverRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email is already registered: " + request.getEmail());
        }
        if (driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new DuplicateResourceException("Driver license number is already registered: " + request.getLicenseNumber());
        }

        Driver driver = new Driver(
                request.getUserId(),
                request.getFullName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getLicenseNumber(),
                request.getYearsOfExperience(),
                request.getServiceCity()
        );

        if (request.getOperatingRadiusKm() != null && request.getOperatingRadiusKm() > 0) {
            driver.setOperatingRadiusKm(request.getOperatingRadiusKm());
        }

        driver.setCreatedAt(LocalDateTime.now());
        driver.setUpdatedAt(LocalDateTime.now());

        Driver saved = driverRepository.save(driver);
        return DriverResponse.fromEntity(saved);
    }

    @Override
    public DriverResponse getDriverById(String driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));
        return enrichDriverResponse(driver);
    }

    @Override
    public DriverResponse getDriverByUserId(String userId) {
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with User ID: " + userId));
        return enrichDriverResponse(driver);
    }

    @Override
    public DriverResponse updateDriverProfile(String driverId, DriverUpdateRequest request) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            driver.setFullName(request.getFullName().trim());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            driver.setPhoneNumber(request.getPhoneNumber().trim());
        }
        if (request.getYearsOfExperience() != null) {
            driver.setYearsOfExperience(request.getYearsOfExperience());
        }
        if (request.getServiceCity() != null && !request.getServiceCity().isBlank()) {
            driver.setServiceCity(request.getServiceCity().trim());
        }
        if (request.getOperatingRadiusKm() != null && request.getOperatingRadiusKm() > 0) {
            driver.setOperatingRadiusKm(request.getOperatingRadiusKm());
        }

        driver.setUpdatedAt(LocalDateTime.now());
        Driver updated = driverRepository.save(driver);
        return enrichDriverResponse(updated);
    }

    @Override
    public DriverResponse updateVerificationStatus(String driverId, VerificationStatus status) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        driver.setVerificationStatus(status);
        if (status != VerificationStatus.VERIFIED) {
            driver.setAvailabilityStatus(AvailabilityStatus.OFFLINE);
        }
        driver.setUpdatedAt(LocalDateTime.now());
        Driver updated = driverRepository.save(driver);
        return enrichDriverResponse(updated);
    }

    @Override
    public DriverResponse updateAvailabilityStatus(String driverId, AvailabilityStatus status) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        if (status == AvailabilityStatus.AVAILABLE) {
            if (driver.getVerificationStatus() != VerificationStatus.VERIFIED) {
                throw new InvalidOperationException("Driver account must be VERIFIED before setting status to AVAILABLE. Current status: " + driver.getVerificationStatus());
            }
            if (driver.getActiveVehicleId() == null) {
                throw new InvalidOperationException("Driver must register and activate a vehicle before setting status to AVAILABLE");
            }
            if (driver.getCurrentLocation() == null) {
                throw new InvalidOperationException("Driver must update current location before going online");
            }
        }

        driver.setAvailabilityStatus(status);
        driver.setUpdatedAt(LocalDateTime.now());
        Driver updated = driverRepository.save(driver);
        return enrichDriverResponse(updated);
    }

    @Override
    public DriverResponse updateLocation(String driverId, LocationUpdateRequest request) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        Location location = new Location(request.getLatitude(), request.getLongitude(), request.getAddressName());
        driver.setCurrentLocation(location);
        driver.setUpdatedAt(LocalDateTime.now());

        Driver updated = driverRepository.save(driver);
        return enrichDriverResponse(updated);
    }

    @Override
    public DriverResponse rateDriver(String driverId, DriverRatingRequest request) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        int newTotal = driver.getTotalRatings() + 1;
        double newRating = ((driver.getRating() * driver.getTotalRatings()) + request.getRating()) / newTotal;
        driver.setRating(Math.round(newRating * 100.0) / 100.0);
        driver.setTotalRatings(newTotal);
        driver.setUpdatedAt(LocalDateTime.now());

        Driver updated = driverRepository.save(driver);
        return enrichDriverResponse(updated);
    }

    @Override
    public List<EligibleDriverResponse> findEligibleDrivers(Double pickupLat, Double pickupLng, VehicleType vehicleType, Double radiusKm, Integer limit) {
        double searchRadius = (radiusKm != null && radiusKm > 0) ? radiusKm : 15.0;
        int maxResults = (limit != null && limit > 0) ? limit : 10;

        List<Driver> availableDrivers = driverRepository.findByAvailabilityStatusAndVerificationStatus(
                AvailabilityStatus.AVAILABLE,
                VerificationStatus.VERIFIED
        );

        List<EligibleDriverResponse> eligibleList = new ArrayList<>();

        for (Driver driver : availableDrivers) {
            if (driver.getCurrentLocation() == null || driver.getActiveVehicleId() == null) {
                continue;
            }

            Optional<Vehicle> optVehicle = vehicleRepository.findById(driver.getActiveVehicleId());
            if (optVehicle.isEmpty()) {
                continue;
            }

            Vehicle vehicle = optVehicle.get();
            if (vehicle.getStatus() != VehicleStatus.ACTIVE) {
                continue;
            }

            if (vehicleType != null && vehicle.getVehicleType() != vehicleType) {
                continue;
            }

            double distance = DistanceCalculator.calculateDistanceKm(
                    pickupLat,
                    pickupLng,
                    driver.getCurrentLocation().getLatitude(),
                    driver.getCurrentLocation().getLongitude()
            );

            double maxAllowedRadius = Math.min(searchRadius, driver.getOperatingRadiusKm());

            if (distance <= maxAllowedRadius) {
                int eta = DistanceCalculator.estimateArrivalMinutes(distance);
                EligibleDriverResponse eligible = new EligibleDriverResponse(
                        driver.getId(),
                        driver.getUserId(),
                        driver.getFullName(),
                        driver.getPhoneNumber(),
                        driver.getRating(),
                        driver.getTotalRatings(),
                        vehicle.getId(),
                        vehicle.getMake(),
                        vehicle.getModel(),
                        vehicle.getPlateNumber(),
                        vehicle.getVehicleType(),
                        vehicle.getColor(),
                        driver.getCurrentLocation().getLatitude(),
                        driver.getCurrentLocation().getLongitude(),
                        distance,
                        eta
                );
                eligibleList.add(eligible);
            }
        }

        return eligibleList.stream()
                .sorted(Comparator.comparingDouble(EligibleDriverResponse::getDistanceKm))
                .limit(maxResults)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponse setInternalDriverStatus(String driverId, AvailabilityStatus status) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));

        driver.setAvailabilityStatus(status);
        driver.setUpdatedAt(LocalDateTime.now());
        Driver updated = driverRepository.save(driver);
        return enrichDriverResponse(updated);
    }

    @Override
    public List<DriverResponse> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(this::enrichDriverResponse)
                .collect(Collectors.toList());
    }

    private DriverResponse enrichDriverResponse(Driver driver) {
        DriverResponse resp = DriverResponse.fromEntity(driver);
        if (driver.getActiveVehicleId() != null) {
            vehicleRepository.findById(driver.getActiveVehicleId())
                    .ifPresent(v -> resp.setActiveVehicle(VehicleResponse.fromEntity(v)));
        }
        return resp;
    }
}
