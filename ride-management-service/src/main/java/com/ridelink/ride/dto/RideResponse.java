package com.ridelink.ride.dto;

import com.ridelink.ride.model.LocationPoint;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.model.VehicleType;

import java.time.LocalDateTime;

public class RideResponse {

    private String id;
    private String passengerId;
    private String driverId;
    private String driverName;
    private String driverPhone;
    private String vehiclePlateNumber;
    private String vehicleModel;
    private LocationPoint pickupLocation;
    private LocationPoint destinationLocation;
    private VehicleType requestedVehicleType;
    private RideStatus status;
    private Double estimatedDistanceKm;
    private Double estimatedFare;
    private Double actualFare;
    private String paymentId;
    private String paymentStatus;
    private String cancellationReason;
    private LocalDateTime requestedAt;
    private LocalDateTime assignedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RideResponse() {
    }

    public static RideResponse fromEntity(Ride ride) {
        RideResponse resp = new RideResponse();
        resp.setId(ride.getId());
        resp.setPassengerId(ride.getPassengerId());
        resp.setDriverId(ride.getDriverId());
        resp.setDriverName(ride.getDriverName());
        resp.setDriverPhone(ride.getDriverPhone());
        resp.setVehiclePlateNumber(ride.getVehiclePlateNumber());
        resp.setVehicleModel(ride.getVehicleModel());
        resp.setPickupLocation(ride.getPickupLocation());
        resp.setDestinationLocation(ride.getDestinationLocation());
        resp.setRequestedVehicleType(ride.getRequestedVehicleType());
        resp.setStatus(ride.getStatus());
        resp.setEstimatedDistanceKm(ride.getEstimatedDistanceKm());
        resp.setEstimatedFare(ride.getEstimatedFare());
        resp.setActualFare(ride.getActualFare());
        resp.setPaymentId(ride.getPaymentId());
        resp.setPaymentStatus(ride.getPaymentStatus());
        resp.setCancellationReason(ride.getCancellationReason());
        resp.setRequestedAt(ride.getRequestedAt());
        resp.setAssignedAt(ride.getAssignedAt());
        resp.setAcceptedAt(ride.getAcceptedAt());
        resp.setStartedAt(ride.getStartedAt());
        resp.setCompletedAt(ride.getCompletedAt());
        resp.setCancelledAt(ride.getCancelledAt());
        resp.setCreatedAt(ride.getCreatedAt());
        resp.setUpdatedAt(ride.getUpdatedAt());
        return resp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getVehiclePlateNumber() {
        return vehiclePlateNumber;
    }

    public void setVehiclePlateNumber(String vehiclePlateNumber) {
        this.vehiclePlateNumber = vehiclePlateNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public LocationPoint getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(LocationPoint pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public LocationPoint getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(LocationPoint destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public VehicleType getRequestedVehicleType() {
        return requestedVehicleType;
    }

    public void setRequestedVehicleType(VehicleType requestedVehicleType) {
        this.requestedVehicleType = requestedVehicleType;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public Double getEstimatedDistanceKm() {
        return estimatedDistanceKm;
    }

    public void setEstimatedDistanceKm(Double estimatedDistanceKm) {
        this.estimatedDistanceKm = estimatedDistanceKm;
    }

    public Double getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(Double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public Double getActualFare() {
        return actualFare;
    }

    public void setActualFare(Double actualFare) {
        this.actualFare = actualFare;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
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
