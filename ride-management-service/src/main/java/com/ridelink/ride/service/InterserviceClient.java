package com.ridelink.ride.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridelink.ride.dto.EligibleDriverDTO;
import com.ridelink.ride.dto.PaymentProcessRequestDTO;
import com.ridelink.ride.dto.PaymentProcessResponseDTO;
import com.ridelink.ride.model.VehicleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class InterserviceClient {

    private static final Logger log = LoggerFactory.getLogger(InterserviceClient.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${ridelink.services.driver-service.url:http://localhost:8082}")
    private String driverServiceUrl;

    @Value("${ridelink.services.fare-service.url:http://localhost:8084}")
    private String fareServiceUrl;

    public InterserviceClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Interservice Call: Query eligible drivers from Driver & Vehicle Service
     */
    public List<EligibleDriverDTO> fetchEligibleDrivers(Double pickupLat, Double pickupLng, VehicleType vehicleType, Double radiusKm, Integer limit) {
        String url = String.format("%s/api/v1/drivers/eligible?pickupLat=%f&pickupLng=%f&radiusKm=%f&limit=%d%s",
                driverServiceUrl,
                pickupLat,
                pickupLng,
                radiusKm != null ? radiusKm : 10.0,
                limit != null ? limit : 5,
                vehicleType != null ? "&vehicleType=" + vehicleType.name() : "");

        try {
            log.info("Calling Driver & Vehicle Service: {}", url);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode dataNode = root.get("data");
                if (dataNode != null && dataNode.isArray()) {
                    List<EligibleDriverDTO> drivers = new ArrayList<>();
                    for (JsonNode item : dataNode) {
                        drivers.add(objectMapper.treeToValue(item, EligibleDriverDTO.class));
                    }
                    return drivers;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to communicate with Driver Service at {}: {}. Falling back to empty driver list.", url, e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * Interservice Call: Update driver availability status in Driver & Vehicle Service
     */
    public boolean updateDriverStatus(String driverId, String status) {
        String url = String.format("%s/api/v1/drivers/%s/status?status=%s", driverServiceUrl, driverId, status);
        try {
            log.info("Updating Driver status in Driver Service: {}", url);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, entity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.warn("Failed to update driver status in Driver Service: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Interservice Call: Trigger simulated payment in Fare & Payment Service
     */
    public PaymentProcessResponseDTO processPayment(PaymentProcessRequestDTO request) {
        String url = String.format("%s/api/v1/payments/process", fareServiceUrl);
        try {
            log.info("Calling Fare & Payment Service: {}", url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PaymentProcessRequestDTO> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode dataNode = root.get("data");
                if (dataNode != null) {
                    return objectMapper.treeToValue(dataNode, PaymentProcessResponseDTO.class);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to communicate with Fare & Payment Service: {}", e.getMessage());
        }

        // Fallback simulation if payment service is unreachable
        PaymentProcessResponseDTO fallback = new PaymentProcessResponseDTO();
        fallback.setPaymentId("PAY-SIM-" + System.currentTimeMillis());
        fallback.setRideId(request.getRideId());
        fallback.setAmount(request.getAmount());
        fallback.setStatus("COMPLETED");
        fallback.setReceiptNumber("REC-" + System.currentTimeMillis());
        return fallback;
    }
}
