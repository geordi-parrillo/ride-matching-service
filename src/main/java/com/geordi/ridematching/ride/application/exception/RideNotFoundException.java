package com.geordi.ridematching.ride.application.exception;

import java.util.UUID;

public final class RideNotFoundException extends RuntimeException {

    public RideNotFoundException(UUID rideId) {
        super("Ride not found: " + rideId);
    }
}
