package com.ridelink.driver.dto;

import com.ridelink.driver.model.VehicleType;

public class EligibleDriverResponse {

    private String driverId;
    private String userId;
    private String fullName;
    private String phoneNumber;
    private Double rating;
    private Integer totalRatings;
    private String vehicleId;
    private String vehicleMake;
    private String vehicleModel;
    private String plateNumber;
    private VehicleType vehicleType;
    private String color;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double distanceKm;
    private Integer estimatedArrivalMinutes;

    public EligibleDriverResponse() {
    }

    public EligibleDriverResponse(String driverId, String userId, String fullName, String phoneNumber, Double rating, Integer totalRatings, String vehicleId, String vehicleMake, String vehicleModel, String plateNumber, VehicleType vehicleType, String color, Double currentLatitude, Double currentLongitude, Double distanceKm, Integer estimatedArrivalMinutes) {
        this.driverId = driverId;
        this.userId = userId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.totalRatings = totalRatings;
        this.vehicleId = vehicleId;
        this.vehicleMake = vehicleMake;
        this.vehicleModel = vehicleModel;
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
        this.color = color;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.distanceKm = distanceKm;
        this.estimatedArrivalMinutes = estimatedArrivalMinutes;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(Integer totalRatings) {
        this.totalRatings = totalRatings;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Integer getEstimatedArrivalMinutes() {
        return estimatedArrivalMinutes;
    }

    public void setEstimatedArrivalMinutes(Integer estimatedArrivalMinutes) {
        this.estimatedArrivalMinutes = estimatedArrivalMinutes;
    }
}
