package com.ridelink.fare.service;

import com.ridelink.fare.dto.PaymentProcessRequest;
import com.ridelink.fare.dto.PaymentResponse;
import com.ridelink.fare.dto.ReceiptResponse;
import com.ridelink.fare.exception.PaymentProcessingException;
import com.ridelink.fare.model.Payment;
import com.ridelink.fare.model.PaymentMethod;
import com.ridelink.fare.model.PaymentStatus;
import com.ridelink.fare.repository.PaymentRepository;
import com.ridelink.fare.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment samplePayment;

    @BeforeEach
    void setUp() {
        samplePayment = new Payment(
                "PAY-112233",
                "ride-001",
                "passenger-101",
                "driver-202",
                550.0,
                PaymentMethod.CREDIT_CARD,
                PaymentStatus.COMPLETED,
                "REC-2026-9999"
        );
    }

    @Test
    @DisplayName("Should successfully process valid payment")
    void testProcessPayment_Success() {
        PaymentProcessRequest req = new PaymentProcessRequest(
                "ride-001", "passenger-101", "driver-202", 550.0, PaymentMethod.CREDIT_CARD, "4111222233334444"
        );

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));

        PaymentResponse response = paymentService.processPayment(req);

        assertNotNull(response);
        assertEquals(PaymentStatus.COMPLETED, response.getStatus());
        assertEquals(550.0, response.getAmount());
        assertNotNull(response.getReceiptNumber());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Negative Scenario: Should fail payment when card number ends in 0000")
    void testProcessPayment_DeclinedCard_ThrowsException() {
        PaymentProcessRequest req = new PaymentProcessRequest(
                "ride-002", "passenger-101", "driver-202", 550.0, PaymentMethod.CREDIT_CARD, "4111222233330000"
        );

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArgument(0));

        assertThrows(PaymentProcessingException.class, () -> paymentService.processPayment(req));
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    @DisplayName("Should retrieve receipt breakdown accurately")
    void testGetReceipt_Success() {
        when(paymentRepository.findByReceiptNumber("REC-2026-9999")).thenReturn(Optional.of(samplePayment));

        ReceiptResponse receipt = paymentService.getReceipt("REC-2026-9999");

        assertNotNull(receipt);
        assertEquals("REC-2026-9999", receipt.getReceiptNumber());
        assertEquals(550.0, receipt.getTotalAmount());
        assertTrue(receipt.getSubtotal() > 0);
        assertTrue(receipt.getTaxAmount() > 0);
        assertEquals(Math.round((receipt.getSubtotal() + receipt.getTaxAmount()) * 100.0) / 100.0, receipt.getTotalAmount());
    }
}
