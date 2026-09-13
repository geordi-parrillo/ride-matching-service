package com.geordi.ridematching.ride.infrastructure.web.dto;

import java.util.UUID;

import com.geordi.ridematching.ride.domain.model.Ride;
import com.geordi.ridematching.shared.domain.model.Location;

public record RideResponse(
        UUID id,
        UUID riderId,
        UUID driverId,
        double pickupX,
        double pickupY,
        String status) {

    public static RideResponse from(Ride ride) {
        Location pickupLocation = ride.getPickupLocation();
        return new RideResponse(
                ride.getId(),
                ride.getRiderId(),
                ride.getDriverId(),
                pickupLocation.x(),
                pickupLocation.y(),
                ride.getStatus().name()
        );
    }
}
