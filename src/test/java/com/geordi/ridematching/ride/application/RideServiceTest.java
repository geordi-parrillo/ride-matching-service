package com.geordi.ridematching.ride.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.driver.domain.repository.DriverRepository;
import com.geordi.ridematching.ride.application.exception.NoDriverAvailableException;
import com.geordi.ridematching.ride.domain.event.RideCompletedEvent;
import com.geordi.ridematching.ride.domain.model.Ride;
import com.geordi.ridematching.ride.domain.model.RideStatus;
import com.geordi.ridematching.ride.domain.repository.RideRepository;
import com.geordi.ridematching.shared.domain.model.Location;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private RideService rideService;

    @Test
    void create_shouldPersistRideWhenDriverIsAvailable() {
        UUID riderId = UUID.randomUUID();
        Driver driver = Driver.create(1.0, 2.0, true);
        Ride expectedRide = Ride.create(riderId, driver.getId(), 5.0, 6.0);

        when(driverRepository.findAndLockNearestAvailable(any(Location.class)))
            .thenReturn(Optional.of(driver));
        when(rideRepository.save(any(Ride.class))).thenReturn(expectedRide);

        Ride result = rideService.create(riderId, 5.0, 6.0);

        assertThat(result).isEqualTo(expectedRide);
        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void create_shouldThrowWhenNoDriverIsAvailable() {
        UUID riderId = UUID.randomUUID();

        when(driverRepository.findAndLockNearestAvailable(any(Location.class)))
            .thenReturn(Optional.empty());

        assertThatThrownBy(() -> rideService.create(riderId, 5.0, 6.0))
            .isInstanceOf(NoDriverAvailableException.class);
    }

    @Test
    void markAsComplete_shouldCompleteRideAndPublishEvent() {
        UUID riderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();
        Ride ride = Ride.create(riderId, driverId, 1.0, 2.0);
        Ride completedRide = ride.complete();

        when(rideRepository.findById(ride.getId())).thenReturn(Optional.of(ride));
        when(rideRepository.save(any(Ride.class))).thenReturn(completedRide);

        Ride result = rideService.markAsComplete(ride.getId());

        assertThat(result.getStatus()).isEqualTo(RideStatus.COMPLETED);
        assertThat(result.getDriverId()).isEqualTo(driverId);

        ArgumentCaptor<RideCompletedEvent> captor = ArgumentCaptor.forClass(RideCompletedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().driverId()).isEqualTo(driverId);
        assertThat(captor.getValue().rideId()).isEqualTo(ride.getId());
    }
}
