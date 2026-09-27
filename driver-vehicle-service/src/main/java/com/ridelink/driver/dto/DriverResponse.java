package com.ridelink.driver.dto;

import com.ridelink.driver.model.AvailabilityStatus;
import com.ridelink.driver.model.Driver;
import com.ridelink.driver.model.Location;
import com.ridelink.driver.model.VerificationStatus;

import java.time.LocalDateTime;

public class DriverResponse {

    private String id;
    private String userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String licenseNumber;
    private Integer yearsOfExperience;
    private Double rating;
    private Integer totalRatings;
    private VerificationStatus verificationStatus;
    private AvailabilityStatus availabilityStatus;
    private String serviceCity;
    private Double operatingRadiusKm;
    private Location currentLocation;
    private String activeVehicleId;
    private VehicleResponse activeVehicle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DriverResponse() {
    }

    public static DriverResponse fromEntity(Driver driver) {
        DriverResponse resp = new DriverResponse();
        resp.setId(driver.getId());
        resp.setUserId(driver.getUserId());
        resp.setFullName(driver.getFullName());
        resp.setEmail(driver.getEmail());
        resp.setPhoneNumber(driver.getPhoneNumber());
        resp.setLicenseNumber(driver.getLicenseNumber());
        resp.setYearsOfExperience(driver.getYearsOfExperience());
        resp.setRating(driver.getRating());
        resp.setTotalRatings(driver.getTotalRatings());
        resp.setVerificationStatus(driver.getVerificationStatus());
        resp.setAvailabilityStatus(driver.getAvailabilityStatus());
        resp.setServiceCity(driver.getServiceCity());
        resp.setOperatingRadiusKm(driver.getOperatingRadiusKm());
        resp.setCurrentLocation(driver.getCurrentLocation());
        resp.setActiveVehicleId(driver.getActiveVehicleId());
        resp.setCreatedAt(driver.getCreatedAt());
        resp.setUpdatedAt(driver.getUpdatedAt());
        return resp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
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

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getServiceCity() {
        return serviceCity;
    }

    public void setServiceCity(String serviceCity) {
        this.serviceCity = serviceCity;
    }

    public Double getOperatingRadiusKm() {
        return operatingRadiusKm;
    }

    public void setOperatingRadiusKm(Double operatingRadiusKm) {
        this.operatingRadiusKm = operatingRadiusKm;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getActiveVehicleId() {
        return activeVehicleId;
    }

    public void setActiveVehicleId(String activeVehicleId) {
        this.activeVehicleId = activeVehicleId;
    }

    public VehicleResponse getActiveVehicle() {
        return activeVehicle;
    }

    public void setActiveVehicle(VehicleResponse activeVehicle) {
        this.activeVehicle = activeVehicle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
