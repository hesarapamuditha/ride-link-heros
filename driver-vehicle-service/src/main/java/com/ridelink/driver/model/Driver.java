package com.ridelink.driver.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "drivers")
public class Driver {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId;

    private String fullName;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;

    @Indexed(unique = true)
    private String licenseNumber;

    private Integer yearsOfExperience;

    private Double rating = 5.0;

    private Integer totalRatings = 0;

    private VerificationStatus verificationStatus = VerificationStatus.PENDING_VERIFICATION;

    private AvailabilityStatus availabilityStatus = AvailabilityStatus.OFFLINE;

    private String serviceCity;

    private Double operatingRadiusKm = 15.0;

    private Location currentLocation;

    private String activeVehicleId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Driver() {
    }

    public Driver(String userId, String fullName, String email, String phoneNumber, String licenseNumber, Integer yearsOfExperience, String serviceCity) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.yearsOfExperience = yearsOfExperience;
        this.serviceCity = serviceCity;
        this.rating = 5.0;
        this.totalRatings = 0;
        this.verificationStatus = VerificationStatus.PENDING_VERIFICATION;
        this.availabilityStatus = AvailabilityStatus.OFFLINE;
        this.operatingRadiusKm = 15.0;
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
