package com.ridelink.account.dto;

import jakarta.validation.constraints.Pattern;

public class UserUpdateRequest {

    private String fullName;

    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Phone number must be valid with 9-15 digits")
    private String phoneNumber;

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
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
}
