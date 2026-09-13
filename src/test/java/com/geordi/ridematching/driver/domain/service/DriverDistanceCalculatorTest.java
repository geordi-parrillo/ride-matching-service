package com.geordi.ridematching.driver.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.shared.domain.model.Location;

class DriverDistanceCalculatorTest {

    @Test
    void nearest_shouldReturnClosestDriversOrderedByDistance() {
        DriverDistanceCalculator calculator = new DriverDistanceCalculator();
        Driver near = Driver.create(0.0, 0.0, true);
        Driver far = Driver.create(10.0, 0.0, true);
        Driver alsoNear = Driver.create(0.0, 1.0, true);
        Location pickup = Location.create(0.0, 0.0);

        List<Driver> nearest = calculator.nearest(List.of(far, near, alsoNear), pickup, 2);

        assertThat(nearest).containsExactly(near, alsoNear);
    }
}
