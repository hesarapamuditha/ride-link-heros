package com.ridelink.driver.dto;

import com.ridelink.driver.model.Vehicle;
import com.ridelink.driver.model.VehicleStatus;
import com.ridelink.driver.model.VehicleType;

import java.time.LocalDateTime;

public class VehicleResponse {

    private String id;
    private String driverId;
    private String make;
    private String model;
    private Integer year;
    private String plateNumber;
    private String color;
    private VehicleType vehicleType;
    private Integer seatingCapacity;
    private VehicleStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public VehicleResponse() {
    }

    public static VehicleResponse fromEntity(Vehicle vehicle) {
        VehicleResponse resp = new VehicleResponse();
        resp.setId(vehicle.getId());
        resp.setDriverId(vehicle.getDriverId());
        resp.setMake(vehicle.getMake());
        resp.setModel(vehicle.getModel());
        resp.setYear(vehicle.getYear());
        resp.setPlateNumber(vehicle.getPlateNumber());
        resp.setColor(vehicle.getColor());
        resp.setVehicleType(vehicle.getVehicleType());
        resp.setSeatingCapacity(vehicle.getSeatingCapacity());
        resp.setStatus(vehicle.getStatus());
        resp.setCreatedAt(vehicle.getCreatedAt());
        resp.setUpdatedAt(vehicle.getUpdatedAt());
        return resp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(Integer seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
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
