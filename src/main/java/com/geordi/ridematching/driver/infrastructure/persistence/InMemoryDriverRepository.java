package com.geordi.ridematching.driver.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Repository;

import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.driver.domain.repository.DriverRepository;
import com.geordi.ridematching.driver.domain.service.DriverDistanceCalculator;
import com.geordi.ridematching.shared.domain.model.Location;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InMemoryDriverRepository implements DriverRepository {

	private final ConcurrentMap<UUID, Driver> drivers = new ConcurrentHashMap<>();
	private final ReentrantLock lock = new ReentrantLock();

	private final DriverDistanceCalculator distanceCalculator;

	@Override
	public Driver save(Driver driver) {
		drivers.put(driver.getId(), driver);
		return driver;
	}

	@Override
	public Optional<Driver> updateAvailability(UUID driverId, boolean available) {
		Driver driver = drivers.computeIfPresent(driverId, 
				(id, currentDriver) -> available ? currentDriver.markAsAvailable() : currentDriver.markAsUnavailable()
		);
		return Optional.ofNullable(driver);
	}

	@Override
	public Optional<Driver> findById(UUID driverId) {
		return Optional.ofNullable(drivers.get(driverId));
	}

	@Override
	public List<Driver> findNearestAvailable(Location location, int limit) {
		List<Driver> availableDrivers = drivers.values().stream()
				.filter(driver -> driver.isAvailable())
				.toList();
		return distanceCalculator.nearest(availableDrivers, location, limit) ;
	}

	@Override
	public Optional<Driver> findAndLockNearestAvailable(Location location) {
        lock.lock();
        try {
			List<Driver> availableDrivers = drivers.values().stream()
				.filter(driver -> driver.isAvailable())
				.toList();

            Optional<Driver> nearest = distanceCalculator.nearest(availableDrivers, location, 1).stream()
				.findFirst();

			return nearest.flatMap(driver -> updateAvailability(driver.getId(), false));
        } finally {
            lock.unlock();
        }
    }
}