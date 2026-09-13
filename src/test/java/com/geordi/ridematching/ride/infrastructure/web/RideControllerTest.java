package com.geordi.ridematching.ride.infrastructure.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.geordi.ridematching.ride.application.RideService;
import com.geordi.ridematching.ride.domain.model.Ride;
import com.geordi.ridematching.ride.infrastructure.web.dto.RideRequest;
import com.geordi.ridematching.ride.infrastructure.web.dto.RideResponse;

@ExtendWith(MockitoExtension.class)
class RideControllerTest {

    @Mock
    private RideService rideService;

    @InjectMocks
    private RideController controller;

    @Test
    void create_shouldReturnRideResponse() {
        UUID riderId = UUID.randomUUID();
        RideRequest request = new RideRequest(riderId, 10.0, 20.0);
        Ride ride = Ride.create(riderId, UUID.randomUUID(), 10.0, 20.0);
        when(rideService.create(request.riderId(), request.x(), request.y())).thenReturn(ride);

        RideResponse response = controller.create(request);

        assertThat(response.id()).isEqualTo(ride.getId());
        assertThat(response.riderId()).isEqualTo(riderId);
        assertThat(response.pickupX()).isEqualTo(10.0);
        assertThat(response.pickupY()).isEqualTo(20.0);
        assertThat(response.status()).isEqualTo("ONGOING");
    }

    @Test
    void markAsComplete_shouldReturnCompletedRideResponse() {
        UUID rideId = UUID.randomUUID();
        UUID riderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();
        Ride ride = Ride.create(riderId, driverId, 11.0, 12.0).complete();
        when(rideService.markAsComplete(rideId)).thenReturn(ride);

        RideResponse response = controller.markAsComplete(rideId);

        assertThat(response.id()).isEqualTo(ride.getId());
        assertThat(response.driverId()).isEqualTo(driverId);
        assertThat(response.status()).isEqualTo("COMPLETED");
    }
}
