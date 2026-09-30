package com.ridelink.ride.service.impl;

import com.ridelink.ride.dto.EligibleDriverDTO;
import com.ridelink.ride.dto.PaymentProcessRequestDTO;
import com.ridelink.ride.dto.PaymentProcessResponseDTO;
import com.ridelink.ride.dto.RideRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.exception.InvalidStateTransitionException;
import com.ridelink.ride.exception.NoDriverAvailableException;
import com.ridelink.ride.exception.ResourceNotFoundException;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.repository.RideRepository;
import com.ridelink.ride.service.InterserviceClient;
import com.ridelink.ride.service.RideService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final InterserviceClient interserviceClient;

    public RideServiceImpl(RideRepository rideRepository, InterserviceClient interserviceClient) {
        this.rideRepository = rideRepository;
        this.interserviceClient = interserviceClient;
    }

    @Override
    public RideResponse requestRide(RideRequest request) {
        Ride ride = new Ride(
                request.getPassengerId(),
                request.getPickupLocation(),
                request.getDestinationLocation(),
                request.getRequestedVehicleType()
        );

        // Approximate distance calculation using Haversine
        double dist = calculateApproxDistance(
                request.getPickupLocation().getLatitude(),
                request.getPickupLocation().getLongitude(),
                request.getDestinationLocation().getLatitude(),
                request.getDestinationLocation().getLongitude()
        );
        ride.setEstimatedDistanceKm(dist);

        // Estimated fare calculation: Base 150 LKR + dist * 100 LKR
        double baseRate = switch (request.getRequestedVehicleType()) {
            case BIKE -> 80.0;
            case VAN -> 220.0;
            case SUV -> 250.0;
            default -> 150.0;
        };
        double perKmRate = switch (request.getRequestedVehicleType()) {
            case BIKE -> 50.0;
            case VAN -> 130.0;
            case SUV -> 150.0;
            default -> 95.0;
        };
        double estimatedFare = Math.round((baseRate + (dist * perKmRate)) * 100.0) / 100.0;
        ride.setEstimatedFare(estimatedFare);
        ride.setCreatedAt(LocalDateTime.now());
        ride.setUpdatedAt(LocalDateTime.now());

        Ride saved = rideRepository.save(ride);
        return RideResponse.fromEntity(saved);
    }

    @Override
    public RideResponse assignDriver(String rideId) {
        Ride ride = getRideEntity(rideId);

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidStateTransitionException("Cannot assign driver to ride in status: " + ride.getStatus() + ". Must be REQUESTED.");
        }

        // Interservice Call: Query eligible drivers from Driver & Vehicle Service
        List<EligibleDriverDTO> eligibleDrivers = interserviceClient.fetchEligibleDrivers(
                ride.getPickupLocation().getLatitude(),
                ride.getPickupLocation().getLongitude(),
                ride.getRequestedVehicleType(),
                15.0,
                5
        );

        if (eligibleDrivers.isEmpty()) {
            throw new NoDriverAvailableException("No available " + ride.getRequestedVehicleType() + " drivers found near pickup area");
        }

        // Documented Algorithm: Select nearest eligible driver (first entry is closest)
        EligibleDriverDTO selectedDriver = eligibleDrivers.get(0);

        // Interservice Call: Mark driver as BUSY
        interserviceClient.updateDriverStatus(selectedDriver.getDriverId(), "BUSY");

        ride.setDriverId(selectedDriver.getDriverId());
        ride.setDriverName(selectedDriver.getFullName());
        ride.setDriverPhone(selectedDriver.getPhoneNumber());
        ride.setVehiclePlateNumber(selectedDriver.getPlateNumber());
        ride.setVehicleModel(selectedDriver.getVehicleMake() + " " + selectedDriver.getVehicleModel());
        ride.setStatus(RideStatus.ASSIGNED);
        ride.setAssignedAt(LocalDateTime.now());
        ride.setUpdatedAt(LocalDateTime.now());

        Ride saved = rideRepository.save(ride);
        return RideResponse.fromEntity(saved);
    }

    @Override
    public RideResponse acceptRide(String rideId) {
        Ride ride = getRideEntity(rideId);

        if (ride.getStatus() != RideStatus.ASSIGNED) {
            throw new InvalidStateTransitionException("Cannot accept ride in status: " + ride.getStatus() + ". Driver must be ASSIGNED first.");
        }

        ride.setStatus(RideStatus.ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());
        ride.setUpdatedAt(LocalDateTime.now());

        Ride saved = rideRepository.save(ride);
        return RideResponse.fromEntity(saved);
    }

    @Override
    public RideResponse startRide(String rideId) {
        Ride ride = getRideEntity(rideId);

        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new InvalidStateTransitionException("Cannot start ride in status: " + ride.getStatus() + ". Ride must be in ACCEPTED state.");
        }

        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setStartedAt(LocalDateTime.now());
        ride.setUpdatedAt(LocalDateTime.now());

        Ride saved = rideRepository.save(ride);
        return RideResponse.fromEntity(saved);
    }

    @Override
    public RideResponse completeRide(String rideId) {
        Ride ride = getRideEntity(rideId);

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new InvalidStateTransitionException("Cannot complete ride in status: " + ride.getStatus() + ". Ride must be IN_PROGRESS.");
        }

        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());
        ride.setActualFare(ride.getEstimatedFare());

        // Interservice Call 1: Free up driver in Driver Service
        if (ride.getDriverId() != null) {
            interserviceClient.updateDriverStatus(ride.getDriverId(), "AVAILABLE");
        }

        // Interservice Call 2: Trigger payment simulation in Fare & Payment Service
        PaymentProcessRequestDTO paymentRequest = new PaymentProcessRequestDTO(
                ride.getId(),
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getActualFare(),
                "CREDIT_CARD"
        );
        PaymentProcessResponseDTO paymentResponse = interserviceClient.processPayment(paymentRequest);
        if (paymentResponse != null) {
            ride.setPaymentId(paymentResponse.getPaymentId());
            ride.setPaymentStatus(paymentResponse.getStatus());
        }

        ride.setUpdatedAt(LocalDateTime.now());
        Ride saved = rideRepository.save(ride);
        return RideResponse.fromEntity(saved);
    }

    @Override
    public RideResponse cancelRide(String rideId, String reason) {
        Ride ride = getRideEntity(rideId);

        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new InvalidStateTransitionException("Cannot cancel an already COMPLETED ride");
        }
        if (ride.getStatus() == RideStatus.CANCELLED) {
            throw new InvalidStateTransitionException("Ride is already CANCELLED");
        }

        // If a driver was already assigned, release the driver back to AVAILABLE
        if (ride.getDriverId() != null) {
            interserviceClient.updateDriverStatus(ride.getDriverId(), "AVAILABLE");
        }

        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancellationReason(reason != null ? reason : "Cancelled by user");
        ride.setCancelledAt(LocalDateTime.now());
        ride.setUpdatedAt(LocalDateTime.now());

        Ride saved = rideRepository.save(ride);
        return RideResponse.fromEntity(saved);
    }

    @Override
    public RideResponse getRideById(String rideId) {
        Ride ride = getRideEntity(rideId);
        return RideResponse.fromEntity(ride);
    }

    @Override
    public List<RideResponse> getRidesByPassenger(String passengerId) {
        return rideRepository.findByPassengerId(passengerId).stream()
                .map(RideResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<RideResponse> getRidesByDriver(String driverId) {
        return rideRepository.findByDriverId(driverId).stream()
                .map(RideResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<RideResponse> getAllRides() {
        return rideRepository.findAll().stream()
                .map(RideResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private Ride getRideEntity(String rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));
    }

    private double calculateApproxDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round((6371.0 * c) * 100.0) / 100.0;
    }
}
