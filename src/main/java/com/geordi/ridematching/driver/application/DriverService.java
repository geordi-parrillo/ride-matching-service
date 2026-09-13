package com.geordi.ridematching.driver.application;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.geordi.ridematching.driver.application.exception.DriverNotFoundException;
import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.driver.domain.model.DriverWithDistance;
import com.geordi.ridematching.driver.domain.repository.DriverRepository;
import com.geordi.ridematching.shared.domain.model.Location;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService {

	private final DriverRepository driverRepository;

	public Driver createDriver(double x, double y, boolean available) {
		return driverRepository.save(Driver.create(x, y, available));
	}

	public Driver updateDriver(UUID id, double x, double y, boolean available) {
		Driver driver = driverRepository.findById(id)
				.orElseThrow(() -> new DriverNotFoundException(id));
		return driverRepository.save(driver.update(x, y, available));
	}

	public List<DriverWithDistance> getAvailableDrivers(double x, double y, int limit) {
        Location pickup = Location.create(x, y);
		return driverRepository.findNearestAvailable(pickup, limit).stream()
				.map(driver -> new DriverWithDistance(driver, driver.getLocation().distanceTo(pickup)))
				.toList();
	}
}