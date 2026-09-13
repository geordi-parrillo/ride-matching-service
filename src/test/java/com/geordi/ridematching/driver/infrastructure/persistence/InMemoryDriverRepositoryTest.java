package com.geordi.ridematching.driver.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.driver.domain.service.DriverDistanceCalculator;
import com.geordi.ridematching.shared.domain.model.Location;

class InMemoryDriverRepositoryTest {

    @Test
    void save_and_findById_shouldPersistDriver() {
        InMemoryDriverRepository repository = new InMemoryDriverRepository(new DriverDistanceCalculator());
        Driver driver = Driver.create(10.0, 20.0, true);

        repository.save(driver);

        assertThat(repository.findById(driver.getId())).contains(driver);
    }

    @Test
    void updateAvailability_shouldChangeDriverStatus() {
        InMemoryDriverRepository repository = new InMemoryDriverRepository(new DriverDistanceCalculator());
        Driver available = Driver.create(0.0, 0.0, true);
        repository.save(available);

        Optional<Driver> updated = repository.updateAvailability(available.getId(), false);

        assertThat(updated).isPresent();
        assertThat(updated.orElseThrow().isAvailable()).isFalse();
        assertThat(repository.findById(available.getId()).orElseThrow().isAvailable()).isFalse();
    }

    @Test
    void findNearestAvailable_shouldReturnDriversOrderedByDistance() {
        InMemoryDriverRepository repository = new InMemoryDriverRepository(new DriverDistanceCalculator());
        Driver nearest = Driver.create(0.0, 0.0, true);
        Driver other = Driver.create(10.0, 0.0, true);
        Driver unavailable = Driver.create(0.0, 100.0, false);
        repository.save(nearest);
        repository.save(other);
        repository.save(unavailable);

        List<Driver> result = repository.findNearestAvailable(Location.create(0.0, 0.0), 2);

        assertThat(result).containsExactly(nearest, other);
    }

    @Test
    void findAndLockNearestAvailable_shouldLockNearestDriver() {
        InMemoryDriverRepository repository = new InMemoryDriverRepository(new DriverDistanceCalculator());
        Driver nearest = Driver.create(0.0, 0.0, true);
        Driver other = Driver.create(5.0, 0.0, true);
        repository.save(nearest);
        repository.save(other);

        Optional<Driver> result = repository.findAndLockNearestAvailable(Location.create(0.0, 0.0));

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getId()).isEqualTo(nearest.getId());
        assertThat(repository.findById(nearest.getId()).orElseThrow().isAvailable()).isFalse();
    }
}
