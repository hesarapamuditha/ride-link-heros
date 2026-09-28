package com.ridelink.driver.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class DriverRatingRequest {

    @NotNull(message = "Rating score is required")
    @DecimalMin(value = "1.0", message = "Minimum rating is 1.0")
    @DecimalMax(value = "5.0", message = "Maximum rating is 5.0")
    private Double rating;

    private String comment;

    public DriverRatingRequest() {
    }

    public DriverRatingRequest(Double rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
