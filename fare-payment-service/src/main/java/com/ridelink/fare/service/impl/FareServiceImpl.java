package com.ridelink.fare.service.impl;

import com.ridelink.fare.dto.FareEstimateRequest;
import com.ridelink.fare.dto.FareEstimateResponse;
import com.ridelink.fare.model.VehicleType;
import com.ridelink.fare.service.FareService;
import org.springframework.stereotype.Service;

@Service
public class FareServiceImpl implements FareService {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double AVERAGE_CITY_SPEED_KMH = 30.0;

    @Override
    public FareEstimateResponse estimateFare(FareEstimateRequest request) {
        double distance = calculateDistanceKm(
                request.getPickupLat(),
                request.getPickupLng(),
                request.getDestLat(),
                request.getDestLng()
        );

        int durationMinutes = Math.max(1, (int) Math.ceil((distance / AVERAGE_CITY_SPEED_KMH) * 60.0));

        double baseFare = getBaseFare(request.getVehicleType());
        double perKmRate = getPerKmRate(request.getVehicleType());
        double distanceFare = Math.round((distance * perKmRate) * 100.0) / 100.0;

        double multiplier = (request.getTrafficMultiplier() != null && request.getTrafficMultiplier() > 0)
                ? request.getTrafficMultiplier() : 1.0;

        double totalFare = Math.round(((baseFare + distanceFare) * multiplier) * 100.0) / 100.0;

        String rule = String.format("Rule: [BaseFare (%.2f LKR) + DistanceFare (%.2f km * %.2f LKR/km)] * TrafficMultiplier (%.2fx)",
                baseFare, distance, perKmRate, multiplier);

        return new FareEstimateResponse(
                request.getVehicleType(),
                distance,
                durationMinutes,
                baseFare,
                perKmRate,
                distanceFare,
                multiplier,
                totalFare,
                rule
        );
    }

    private double getBaseFare(VehicleType vehicleType) {
        return switch (vehicleType) {
            case BIKE -> 80.0;
            case VAN -> 220.0;
            case SUV -> 250.0;
            case SEDAN -> 180.0;
            default -> 150.0; // CAR
        };
    }

    private double getPerKmRate(VehicleType vehicleType) {
        return switch (vehicleType) {
            case BIKE -> 50.0;
            case VAN -> 130.0;
            case SUV -> 150.0;
            case SEDAN -> 110.0;
            default -> 95.0; // CAR
        };
    }

    private double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);

        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return Math.round((EARTH_RADIUS_KM * c) * 100.0) / 100.0;
    }
}
