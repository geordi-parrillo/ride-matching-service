package com.geordi.ridematching.ride.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.geordi.ridematching.ride.domain.model.Ride;

class InMemoryRideRepositoryTest {

    @Test
    void save_and_findById_shouldPersistRide() {
        InMemoryRideRepository repository = new InMemoryRideRepository();
        Ride ride = Ride.create(UUID.randomUUID(), UUID.randomUUID(), 10.0, 20.0);

        repository.save(ride);

        Optional<Ride> found = repository.findById(ride.getId());
        assertThat(found).contains(ride);
    }
}
