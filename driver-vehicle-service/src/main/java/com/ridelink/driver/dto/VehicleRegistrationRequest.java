package com.ridelink.driver.dto;

import com.ridelink.driver.model.VehicleType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class VehicleRegistrationRequest {

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Year is required")
    @Min(value = 1995, message = "Vehicle must not be older than 1995")
    @Max(value = 2030, message = "Vehicle year cannot be in the distant future")
    private Integer year;

    @NotBlank(message = "Plate number is required")
    private String plateNumber;

    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotNull(message = "Seating capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 20, message = "Capacity cannot exceed 20")
    private Integer seatingCapacity;

    public VehicleRegistrationRequest() {
    }

    public VehicleRegistrationRequest(String make, String model, Integer year, String plateNumber, String color, VehicleType vehicleType, Integer seatingCapacity) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.plateNumber = plateNumber;
        this.color = color;
        this.vehicleType = vehicleType;
        this.seatingCapacity = seatingCapacity;
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
}
