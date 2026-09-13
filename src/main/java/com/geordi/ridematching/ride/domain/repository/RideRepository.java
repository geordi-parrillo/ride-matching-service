package com.geordi.ridematching.ride.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.geordi.ridematching.ride.domain.model.Ride;

public interface RideRepository {

	Ride save(Ride ride);

	Optional<Ride> findById(UUID rideId);
}