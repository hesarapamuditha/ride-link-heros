package com.ridelink.ride.dto;

public class PaymentProcessRequestDTO {

    private String rideId;
    private String passengerId;
    private String driverId;
    private Double amount;
    private String paymentMethod;

    public PaymentProcessRequestDTO() {
    }

    public PaymentProcessRequestDTO(String rideId, String passengerId, String driverId, Double amount, String paymentMethod) {
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.driverId = driverId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
