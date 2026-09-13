package com.geordi.ridematching.ride.infrastructure.web;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.geordi.ridematching.ride.application.RideService;
import com.geordi.ridematching.ride.domain.model.Ride;
import com.geordi.ridematching.ride.infrastructure.web.dto.RideRequest;
import com.geordi.ridematching.ride.infrastructure.web.dto.RideResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

	private final RideService rideService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RideResponse create(@Valid @RequestBody RideRequest request) {
		Ride ride = rideService.create(request.riderId(), request.x(), request.y());
		return RideResponse.from(ride);
	}

	@PostMapping("{id}/complete")
	public RideResponse markAsComplete(@PathVariable UUID id) {
		Ride ride = rideService.markAsComplete(id);
		return RideResponse.from(ride);
	}
}