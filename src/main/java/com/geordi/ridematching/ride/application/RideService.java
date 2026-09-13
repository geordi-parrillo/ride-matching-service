package com.geordi.ridematching.ride.application;

import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.driver.domain.repository.DriverRepository;
import com.geordi.ridematching.ride.application.exception.NoDriverAvailableException;
import com.geordi.ridematching.ride.application.exception.RideNotFoundException;
import com.geordi.ridematching.ride.domain.event.RideCompletedEvent;
import com.geordi.ridematching.ride.domain.model.Ride;
import com.geordi.ridematching.ride.domain.repository.RideRepository;
import com.geordi.ridematching.shared.domain.model.Location;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class RideService {

	private final RideRepository rideRepository;
	private final DriverRepository driverRepository;
	private final ApplicationEventPublisher eventPublisher;

	public Ride create(UUID riderId, double x, double y) {
		Location pickup = Location.create(x, y);
		Driver driver = driverRepository.findAndLockNearestAvailable(pickup)
			.orElseThrow(() -> new NoDriverAvailableException());
		return rideRepository.save(Ride.create(riderId, driver.getId(), x, y));
	}

	public Ride markAsComplete(UUID rideId) {
		Ride ride = rideRepository.findById(rideId)
			.orElseThrow(() -> new RideNotFoundException(rideId));
		Ride completedRide = rideRepository.save(ride.complete());

		eventPublisher.publishEvent(new RideCompletedEvent(completedRide.getId(), completedRide.getDriverId()));
		
		return completedRide;
	}
}