package com.geordi.ridematching.ride.domain.event;

import java.util.UUID;

public record RideCompletedEvent(UUID rideId, UUID driverId) {

}
