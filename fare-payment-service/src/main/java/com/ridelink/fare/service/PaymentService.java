package com.ridelink.fare.service;

import com.ridelink.fare.dto.PaymentProcessRequest;
import com.ridelink.fare.dto.PaymentResponse;
import com.ridelink.fare.dto.ReceiptResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse processPayment(PaymentProcessRequest request);

    PaymentResponse getPaymentById(String paymentId);

    PaymentResponse getPaymentByRideId(String rideId);

    ReceiptResponse getReceipt(String receiptNumber);

    ReceiptResponse getReceiptByRideId(String rideId);

    List<PaymentResponse> getAllPayments();
}
