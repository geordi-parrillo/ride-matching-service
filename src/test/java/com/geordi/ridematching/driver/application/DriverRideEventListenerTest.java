package com.geordi.ridematching.driver.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.geordi.ridematching.driver.application.exception.DriverNotFoundException;
import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.driver.domain.repository.DriverRepository;
import com.geordi.ridematching.ride.domain.event.RideCompletedEvent;

@ExtendWith(MockitoExtension.class)
class DriverRideEventListenerTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverRideEventListener listener;

    @Test
    void handleRideCompletedEvent_shouldMarkDriverAvailable() {
        UUID driverId = UUID.randomUUID();
        Driver driver = Driver.create(0.0, 0.0, false);

        when(driverRepository.updateAvailability(driverId, true)).thenReturn(Optional.of(driver));

        listener.handleRideCompletedEvent(new RideCompletedEvent(UUID.randomUUID(), driverId));

        verify(driverRepository).updateAvailability(driverId, true);
    }

    @Test
    void handleRideCompletedEvent_shouldThrowWhenDriverIsMissing() {
        UUID driverId = UUID.randomUUID();

        when(driverRepository.updateAvailability(driverId, true)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listener.handleRideCompletedEvent(new RideCompletedEvent(UUID.randomUUID(), driverId)))
            .isInstanceOf(DriverNotFoundException.class);
    }
}
