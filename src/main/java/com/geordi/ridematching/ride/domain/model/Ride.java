package com.geordi.ridematching.ride.domain.model;

import java.util.UUID;

import com.geordi.ridematching.shared.domain.model.Location;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class Ride {

	private final UUID id;
	private final UUID riderId;
	private final UUID driverId;
	private final Location pickupLocation;
	private final RideStatus status;

	public static Ride create(UUID riderId, UUID driverId, double x, double y) {
		if (riderId == null) {
			throw new IllegalArgumentException("riderId must not be null");
		}
		if (driverId == null) {
			throw new IllegalArgumentException("driverId must not be null");
		}
		return new Ride(UUID.randomUUID(), riderId, driverId, Location.create(x, y), RideStatus.ONGOING);
	}

	public Ride complete() {
		if (this.status == RideStatus.COMPLETED) {
			throw new IllegalStateException("Ride is already completed");
		}
		return new Ride(this.id, this.riderId, this.driverId, this.pickupLocation, RideStatus.COMPLETED);
	}
}