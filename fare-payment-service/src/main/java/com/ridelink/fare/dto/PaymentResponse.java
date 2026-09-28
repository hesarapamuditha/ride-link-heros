package com.ridelink.fare.dto;

import com.ridelink.fare.model.Payment;
import com.ridelink.fare.model.PaymentMethod;
import com.ridelink.fare.model.PaymentStatus;

import java.time.LocalDateTime;

public class PaymentResponse {

    private String paymentId;
    private String rideId;
    private String passengerId;
    private String driverId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String receiptNumber;
    private String failureReason;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;

    public PaymentResponse() {
    }

    public static PaymentResponse fromEntity(Payment payment) {
        PaymentResponse resp = new PaymentResponse();
        resp.setPaymentId(payment.getPaymentId());
        resp.setRideId(payment.getRideId());
        resp.setPassengerId(payment.getPassengerId());
        resp.setDriverId(payment.getDriverId());
        resp.setAmount(payment.getAmount());
        resp.setPaymentMethod(payment.getPaymentMethod());
        resp.setStatus(payment.getStatus());
        resp.setReceiptNumber(payment.getReceiptNumber());
        resp.setFailureReason(payment.getFailureReason());
        resp.setPaidAt(payment.getPaidAt());
        resp.setCreatedAt(payment.getCreatedAt());
        return resp;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
