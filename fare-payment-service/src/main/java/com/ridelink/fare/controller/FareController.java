package com.ridelink.fare.controller;

import com.ridelink.fare.dto.ApiResponse;
import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;
import com.ridelink.fare.service.FareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fares")
@Tag(name = "Fare Estimation", description = "Endpoints for transparent fare estimation based on distance and vehicle multipliers")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @PostMapping("/estimate")
    @Operation(summary = "Calculate fare estimate", description = "Computes distance, travel duration, base fare, and total estimated cost using documented pricing rules.")
    public ResponseEntity<ApiResponse<FareEstimateResponse>> estimateFare(@Valid @RequestBody FareEstimateRequest request) {
        FareEstimateResponse response = fareService.estimateFare(request);
        return ResponseEntity.ok(ApiResponse.ok("Fare estimate computed", response));
    }
}
