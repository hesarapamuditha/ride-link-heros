package com.ridelink.fare.dto;

import com.ridelink.fare.model.VehicleType;

public class FareEstimateResponse {

    private VehicleType vehicleType;
    private Double distanceKm;
    private Integer estimatedDurationMinutes;
    private Double baseFare;
    private Double perKmRate;
    private Double distanceFare;
    private Double trafficMultiplier;
    private Double totalEstimatedFare;
    private String calculationRule;

    public FareEstimateResponse() {
    }

    public FareEstimateResponse(VehicleType vehicleType, Double distanceKm, Integer estimatedDurationMinutes, Double baseFare, Double perKmRate, Double distanceFare, Double trafficMultiplier, Double totalEstimatedFare, String calculationRule) {
        this.vehicleType = vehicleType;
        this.distanceKm = distanceKm;
        this.estimatedDurationMinutes = estimatedDurationMinutes;
        this.baseFare = baseFare;
        this.perKmRate = perKmRate;
        this.distanceFare = distanceFare;
        this.trafficMultiplier = trafficMultiplier;
        this.totalEstimatedFare = totalEstimatedFare;
        this.calculationRule = calculationRule;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Integer getEstimatedDurationMinutes() {
        return estimatedDurationMinutes;
    }

    public void setEstimatedDurationMinutes(Integer estimatedDurationMinutes) {
        this.estimatedDurationMinutes = estimatedDurationMinutes;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public Double getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(Double perKmRate) {
        this.perKmRate = perKmRate;
    }

    public Double getDistanceFare() {
        return distanceFare;
    }

    public void setDistanceFare(Double distanceFare) {
        this.distanceFare = distanceFare;
    }

    public Double getTrafficMultiplier() {
        return trafficMultiplier;
    }

    public void setTrafficMultiplier(Double trafficMultiplier) {
        this.trafficMultiplier = trafficMultiplier;
    }

    public Double getTotalEstimatedFare() {
        return totalEstimatedFare;
    }

    public void setTotalEstimatedFare(Double totalEstimatedFare) {
        this.totalEstimatedFare = totalEstimatedFare;
    }

    public String getCalculationRule() {
        return calculationRule;
    }

    public void setCalculationRule(String calculationRule) {
        this.calculationRule = calculationRule;
    }
}
