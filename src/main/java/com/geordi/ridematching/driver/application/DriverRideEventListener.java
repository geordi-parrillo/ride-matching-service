package com.geordi.ridematching.driver.application;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.geordi.ridematching.driver.application.exception.DriverNotFoundException;
import com.geordi.ridematching.driver.domain.repository.DriverRepository;
import com.geordi.ridematching.ride.domain.event.RideCompletedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DriverRideEventListener {

    private final DriverRepository driverRepository;

    @EventListener
    public void handleRideCompletedEvent(RideCompletedEvent event) {
        driverRepository.updateAvailability(event.driverId(), true)
            .orElseThrow(() -> new DriverNotFoundException(event.driverId()));
    }
}
