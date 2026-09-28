package com.ridelink.driver.model;

import java.time.LocalDateTime;

public class Location {
    private Double latitude;
    private Double longitude;
    private String addressName;
    private LocalDateTime lastUpdated;

    public Location() {
        this.lastUpdated = LocalDateTime.now();
    }

    public Location(Double latitude, Double longitude, String addressName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressName = addressName;
        this.lastUpdated = LocalDateTime.now();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
