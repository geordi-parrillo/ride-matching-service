package com.geordi.ridematching.driver.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.shared.domain.model.Location;

public interface DriverRepository {

	Driver save(Driver driver);

	Optional<Driver> updateAvailability(UUID driverId, boolean available);

	Optional<Driver> findById(UUID driverId);

	List<Driver> findNearestAvailable(Location location, int limit);

	Optional<Driver> findAndLockNearestAvailable(Location pickup);
}