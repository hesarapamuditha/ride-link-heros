package com.ridelink.driver.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

public class DriverUpdateRequest {

    private String fullName;

    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Phone number must be valid with 9-15 digits")
    private String phoneNumber;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;

    private String serviceCity;

    @Min(value = 1, message = "Operating radius must be at least 1 km")
    private Double operatingRadiusKm;

    public DriverUpdateRequest() {
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

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
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
}
