package com.geordi.ridematching.driver.domain.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.shared.domain.model.Location;

@Component
public class DriverDistanceCalculator {

	public List<Driver> nearest(List<Driver> drivers, Location location, int limit) {
		return drivers.stream()
				.sorted(Comparator.comparingDouble(driver -> driver.getLocation().distanceTo(location)))
				.limit(limit)
				.toList();
	}
}