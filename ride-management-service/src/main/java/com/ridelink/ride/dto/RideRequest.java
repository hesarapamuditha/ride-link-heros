package com.ridelink.ride.dto;

import com.ridelink.ride.model.LocationPoint;
import com.ridelink.ride.model.VehicleType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RideRequest {

    @NotBlank(message = "Passenger ID is required")
    private String passengerId;

    @NotNull(message = "Pickup location is required")
    @Valid
    private LocationPoint pickupLocation;

    @NotNull(message = "Destination location is required")
    @Valid
    private LocationPoint destinationLocation;

    @NotNull(message = "Vehicle type is required (CAR, VAN, BIKE, SUV, SEDAN)")
    private VehicleType vehicleType;

    public RideRequest() {
    }

    public RideRequest(String passengerId, LocationPoint pickupLocation, LocationPoint destinationLocation, VehicleType vehicleType) {
        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.destinationLocation = destinationLocation;
        this.vehicleType = vehicleType;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public LocationPoint getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(LocationPoint pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public LocationPoint getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(LocationPoint destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public VehicleType getRequestedVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }
}
