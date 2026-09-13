package com.geordi.ridematching.driver.domain.model;

import java.util.UUID;

import com.geordi.ridematching.shared.domain.model.Location;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Driver {

	private UUID id;
	private Location location;
	private boolean available;

	public static Driver create(Double x, Double y, Boolean available) {
        if(available == null) {
            throw new IllegalArgumentException("available must not be null");
        }
		return new Driver(UUID.randomUUID(), Location.create(x, y), available);
	}

    public Driver update(Double x, Double y, Boolean available) {
        if(available == null) {
            throw new IllegalArgumentException("available must not be null");
        }
		return new Driver(this.id, Location.create(x, y), available);
	}

	public Driver markAsUnavailable() {
		return new Driver(this.id, this.location, false);
	}

    public Driver markAsAvailable() {
		return new Driver(this.id, this.location, true);
	}
}