package com.geordi.ridematching.ride.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Repository;

import com.geordi.ridematching.ride.domain.model.Ride;
import com.geordi.ridematching.ride.domain.repository.RideRepository;

@Repository
public class InMemoryRideRepository implements RideRepository {

	private final ConcurrentMap<UUID, Ride> rides = new ConcurrentHashMap<>();

	@Override
	public Ride save(Ride ride) {
		rides.put(ride.getId(), ride);
		return ride;
	}

	@Override
	public Optional<Ride> findById(UUID rideId) {
		return Optional.ofNullable(rides.get(rideId));
	}
}