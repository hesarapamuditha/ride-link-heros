package com.ridelink.fare.dto;

import com.ridelink.fare.model.VehicleType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class FareEstimateRequest {

    @NotNull(message = "Pickup latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90.0")
    private Double pickupLat;

    @NotNull(message = "Pickup longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180.0")
    private Double pickupLng;

    @NotNull(message = "Destination latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be >= -90.0")
    @DecimalMax(value = "90.0", message = "Latitude must be <= 90.0")
    private Double destLat;

    @NotNull(message = "Destination longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be >= -180.0")
    @DecimalMax(value = "180.0", message = "Longitude must be <= 180.0")
    private Double destLng;

    @NotNull(message = "Vehicle type is required (CAR, VAN, BIKE, SUV, SEDAN)")
    private VehicleType vehicleType;

    private Double trafficMultiplier = 1.0;

    public FareEstimateRequest() {
    }

    public FareEstimateRequest(Double pickupLat, Double pickupLng, Double destLat, Double destLng, VehicleType vehicleType) {
        this.pickupLat = pickupLat;
        this.pickupLng = pickupLng;
        this.destLat = destLat;
        this.destLng = destLng;
        this.vehicleType = vehicleType;
        this.trafficMultiplier = 1.0;
    }

    public Double getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(Double pickupLat) {
        this.pickupLat = pickupLat;
    }

    public Double getPickupLng() {
        return pickupLng;
    }

    public void setPickupLng(Double pickupLng) {
        this.pickupLng = pickupLng;
    }

    public Double getDestLat() {
        return destLat;
    }

    public void setDestLat(Double destLat) {
        this.destLat = destLat;
    }

    public Double getDestLng() {
        return destLng;
    }

    public void setDestLng(Double destLng) {
        this.destLng = destLng;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getTrafficMultiplier() {
        return trafficMultiplier;
    }

    public void setTrafficMultiplier(Double trafficMultiplier) {
        this.trafficMultiplier = trafficMultiplier;
    }
}
