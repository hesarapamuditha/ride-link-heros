package com.ridelink.fare.dto;

import com.ridelink.fare.model.Payment;
import com.ridelink.fare.model.PaymentMethod;

import java.time.LocalDateTime;

public class ReceiptResponse {

    private String receiptNumber;
    private String paymentId;
    private String rideId;
    private String passengerId;
    private String driverId;
    private Double subtotal;
    private Double taxAmount;
    private Double totalAmount;
    private PaymentMethod paymentMethod;
    private String status;
    private LocalDateTime issuedAt;

    public ReceiptResponse() {
    }

    public static ReceiptResponse fromPayment(Payment payment) {
        ReceiptResponse r = new ReceiptResponse();
        r.setReceiptNumber(payment.getReceiptNumber());
        r.setPaymentId(payment.getPaymentId());
        r.setRideId(payment.getRideId());
        r.setPassengerId(payment.getPassengerId());
        r.setDriverId(payment.getDriverId());
        double subtotal = Math.round((payment.getAmount() / 1.05) * 100.0) / 100.0;
        double tax = Math.round((payment.getAmount() - subtotal) * 100.0) / 100.0;
        r.setSubtotal(subtotal);
        r.setTaxAmount(tax);
        r.setTotalAmount(payment.getAmount());
        r.setPaymentMethod(payment.getPaymentMethod());
        r.setStatus(payment.getStatus().name());
        r.setIssuedAt(payment.getPaidAt() != null ? payment.getPaidAt() : LocalDateTime.now());
        return r;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
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

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }
}
