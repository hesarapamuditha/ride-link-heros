package com.ridelink.fare.service.impl;

import com.ridelink.fare.dto.PaymentProcessRequest;
import com.ridelink.fare.dto.PaymentResponse;
import com.ridelink.fare.dto.ReceiptResponse;
import com.ridelink.fare.exception.PaymentProcessingException;
import com.ridelink.fare.exception.ResourceNotFoundException;
import com.ridelink.fare.model.Payment;
import com.ridelink.fare.model.PaymentStatus;
import com.ridelink.fare.repository.PaymentRepository;
import com.ridelink.fare.service.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponse processPayment(PaymentProcessRequest request) {
        String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String receiptNumber = "REC-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Payment payment = new Payment(
                paymentId,
                request.getRideId(),
                request.getPassengerId(),
                request.getDriverId(),
                request.getAmount(),
                request.getPaymentMethod(),
                PaymentStatus.COMPLETED,
                receiptNumber
        );

        // Negative Scenario: Simulated payment failure condition
        if (request.getCardNumber() != null &&
                (request.getCardNumber().endsWith("0000") || request.getCardNumber().toUpperCase().contains("FAIL"))) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Simulated card authorization failure: Insufficient funds or declined by issuing bank");
            payment.setReceiptNumber(null);
            payment.setPaidAt(null);
            payment.setCreatedAt(LocalDateTime.now());
            payment.setUpdatedAt(LocalDateTime.now());
            Payment savedFailed = paymentRepository.save(payment);
            throw new PaymentProcessingException("Simulated payment failed: " + savedFailed.getFailureReason());
        }

        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        Payment saved = paymentRepository.save(payment);
        return PaymentResponse.fromEntity(saved);
    }

    @Override
    public PaymentResponse getPaymentById(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentId));
        return PaymentResponse.fromEntity(payment);
    }

    @Override
    public PaymentResponse getPaymentByRideId(String rideId) {
        Payment payment = paymentRepository.findByRideId(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found for Ride ID: " + rideId));
        return PaymentResponse.fromEntity(payment);
    }

    @Override
    public ReceiptResponse getReceipt(String receiptNumber) {
        Payment payment = paymentRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found for receipt number: " + receiptNumber));
        return ReceiptResponse.fromPayment(payment);
    }

    @Override
    public ReceiptResponse getReceiptByRideId(String rideId) {
        Payment payment = paymentRepository.findByRideId(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found for Ride ID: " + rideId));
        return ReceiptResponse.fromPayment(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
