package com.ridelink.driver.dto;

import com.ridelink.driver.model.AvailabilityStatus;
import jakarta.validation.constraints.NotNull;

public class AvailabilityUpdateRequest {

    @NotNull(message = "Availability status is required")
    private AvailabilityStatus status;

    public AvailabilityUpdateRequest() {
    }

    public AvailabilityUpdateRequest(AvailabilityStatus status) {
        this.status = status;
    }

    public AvailabilityStatus getStatus() {
        return status;
    }

    public void setStatus(AvailabilityStatus status) {
        this.status = status;
    }
}
