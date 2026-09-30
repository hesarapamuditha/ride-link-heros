package com.ridelink.ride.service;

import com.ridelink.ride.dto.RideRequest;
import com.ridelink.ride.dto.RideResponse;

import java.util.List;

public interface RideService {

    RideResponse requestRide(RideRequest request);

    RideResponse assignDriver(String rideId);

    RideResponse acceptRide(String rideId);

    RideResponse startRide(String rideId);

    RideResponse completeRide(String rideId);

    RideResponse cancelRide(String rideId, String reason);

    RideResponse getRideById(String rideId);

    List<RideResponse> getRidesByPassenger(String passengerId);

    List<RideResponse> getRidesByDriver(String driverId);

    List<RideResponse> getAllRides();
}
