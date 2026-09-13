package com.geordi.ridematching.ride.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.geordi.ridematching.shared.domain.model.Location;

class RideTest {

    @Test
    void create_shouldCreateOngoingRide() {
        UUID riderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();

        Ride ride = Ride.create(riderId, driverId, 10.0, 20.0);

        assertThat(ride.getId()).isNotNull();
        assertThat(ride.getRiderId()).isEqualTo(riderId);
        assertThat(ride.getDriverId()).isEqualTo(driverId);
        assertThat(ride.getPickupLocation()).isEqualTo(new Location(10.0, 20.0));
        assertThat(ride.getStatus()).isEqualTo(RideStatus.ONGOING);
    }

    @Test
    void create_shouldRejectNullRiderId() {
        UUID driverId = UUID.randomUUID();

        assertThatThrownBy(() -> Ride.create(null, driverId, 10.0, 20.0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_shouldRejectNullDriverId() {
        UUID riderId = UUID.randomUUID();

        assertThatThrownBy(() -> Ride.create(riderId, null, 10.0, 20.0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void complete_shouldMarkRideAsCompleted() {
        Ride ride = Ride.create(UUID.randomUUID(), UUID.randomUUID(), 1.0, 2.0);

        Ride completed = ride.complete();

        assertThat(completed.getId()).isEqualTo(ride.getId());
        assertThat(completed.getStatus()).isEqualTo(RideStatus.COMPLETED);
    }

    @Test
    void complete_shouldFailWhenAlreadyCompleted() {
        Ride completedRide = Ride.create(UUID.randomUUID(), UUID.randomUUID(), 1.0, 2.0).complete();

        assertThatThrownBy(completedRide::complete)
            .isInstanceOf(IllegalStateException.class);
    }
}
