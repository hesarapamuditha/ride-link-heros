package com.ridelink.fare.service;

import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;

public interface FareService {

    FareEstimateResponse estimateFare(FareEstimateRequest request);
}
