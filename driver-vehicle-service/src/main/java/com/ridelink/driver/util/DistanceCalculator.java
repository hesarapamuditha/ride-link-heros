package com.ridelink.driver.util;

public final class DistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double AVERAGE_CITY_SPEED_KMH = 30.0;

    private DistanceCalculator() {
    }

    /**
     * Calculates distance between two latitude/longitude points in kilometers using Haversine formula.
     */
    public static double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);

        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        double distance = EARTH_RADIUS_KM * c;

        // Round to 2 decimal places
        return Math.round(distance * 100.0) / 100.0;
    }

    /**
     * Estimates driver arrival time in minutes based on distance and average city driving speed (30 km/h).
     */
    public static int estimateArrivalMinutes(double distanceKm) {
        if (distanceKm <= 0.0) {
            return 1;
        }
        int minutes = (int) Math.ceil((distanceKm / AVERAGE_CITY_SPEED_KMH) * 60.0);
        return Math.max(1, minutes);
    }
}
