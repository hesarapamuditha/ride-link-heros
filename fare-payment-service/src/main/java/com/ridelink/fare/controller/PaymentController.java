package com.ridelink.fare.controller;

import com.ridelink.fare.dto.ApiResponse;
import com.ridelink.fare.dto.PaymentProcessRequest;
import com.ridelink.fare.dto.PaymentResponse;
import com.ridelink.fare.dto.ReceiptResponse;
import com.ridelink.fare.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment & Receipts", description = "Endpoints for simulated payment processing, payment history, and receipt issuance")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    @Operation(summary = "Process simulated payment", description = "Simulates payment authorization. If cardNumber ends in 0000 or contains FAIL, simulates bank decline (negative scenario).")
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(@Valid @RequestBody PaymentProcessRequest request) {
        PaymentResponse response = paymentService.processPayment(request);
        return new ResponseEntity<>(ApiResponse.ok("Payment processed successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment by ID", description = "Retrieves payment transaction record.")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(@PathVariable String paymentId) {
        PaymentResponse response = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/ride/{rideId}")
    @Operation(summary = "Get payment by Ride ID", description = "Retrieves payment record associated with a specific ride.")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByRideId(@PathVariable String rideId) {
        PaymentResponse response = paymentService.getPaymentByRideId(rideId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/receipts/{receiptNumber}")
    @Operation(summary = "Get receipt by receipt number", description = "Retrieves itemized receipt breakdown including tax and total.")
    public ResponseEntity<ApiResponse<ReceiptResponse>> getReceipt(@PathVariable String receiptNumber) {
        ReceiptResponse response = paymentService.getReceipt(receiptNumber);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/receipts/ride/{rideId}")
    @Operation(summary = "Get receipt by Ride ID", description = "Retrieves itemized receipt for a completed ride.")
    public ResponseEntity<ApiResponse<ReceiptResponse>> getReceiptByRideId(@PathVariable String rideId) {
        ReceiptResponse response = paymentService.getReceiptByRideId(rideId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    @Operation(summary = "Get all payments", description = "Lists all simulated payments (Admin / Auditor).")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(ApiResponse.ok(payments));
    }
}
