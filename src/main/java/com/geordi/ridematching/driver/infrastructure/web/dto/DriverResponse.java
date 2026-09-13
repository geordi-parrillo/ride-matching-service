package com.geordi.ridematching.driver.infrastructure.web.dto;

import java.util.UUID;

import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.driver.domain.model.DriverWithDistance;

public record DriverResponse(
		UUID id,
		double x,
		double y,
		Double distance) {

	public static DriverResponse from(Driver driver) {
		return new DriverResponse(
				driver.getId(),
				driver.getLocation().x(),
				driver.getLocation().y(),
				null		
		);
	}

	public static DriverResponse from(DriverWithDistance driverWithDistance) {
		Driver driver = driverWithDistance.driver();
		return new DriverResponse(
				driver.getId(),
				driver.getLocation().x(),
				driver.getLocation().y(),
				driverWithDistance.distance()
		);
	}
}