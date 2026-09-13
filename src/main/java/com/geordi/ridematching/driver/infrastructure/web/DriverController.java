package com.geordi.ridematching.driver.infrastructure.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.geordi.ridematching.driver.application.DriverService;
import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.driver.infrastructure.web.dto.DriverRequest;
import com.geordi.ridematching.driver.infrastructure.web.dto.DriverResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

	private final DriverService driverService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DriverResponse create(@Valid @RequestBody DriverRequest request) {
		Driver driver = driverService.createDriver(request.x(), request.y(), request.available());
		return DriverResponse.from(driver);
	}

	@PutMapping("/{id}")
	public DriverResponse update(@PathVariable UUID id, @Valid @RequestBody DriverRequest request) {
		Driver driver = driverService.updateDriver(id, request.x(), request.y(), request.available());
		return DriverResponse.from(driver);
	}

	@GetMapping("/available")
	public List<DriverResponse> getAvailableDrivers(
		@RequestParam double x,
		@RequestParam double y,
	    @RequestParam(required = false, defaultValue = "10") int limit) {
		return driverService.getAvailableDrivers(x, y, limit).stream()
				.map(DriverResponse::from)
				.toList();
	}
}