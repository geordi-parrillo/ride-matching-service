package com.geordi.ridematching.driver.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.geordi.ridematching.driver.application.exception.DriverNotFoundException;
import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.driver.domain.model.DriverWithDistance;
import com.geordi.ridematching.driver.domain.repository.DriverRepository;
import com.geordi.ridematching.shared.domain.model.Location;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    @Test
    void createDriver_shouldPersistAndReturnDriver() {
        Driver expected = Driver.create(10.0, 20.0, true);
        when(driverRepository.save(any(Driver.class))).thenReturn(expected);

        Driver result = driverService.createDriver(10.0, 20.0, true);

        assertThat(result).isSameAs(expected);
        verify(driverRepository).save(any(Driver.class));
    }

    @Test
    void updateDriver_shouldUpdateExistingDriver() {
        UUID id = UUID.randomUUID();
        Driver existing = Driver.create(0.0, 0.0, true);
        Driver updated = existing.update(2.0, 3.0, false);
        when(driverRepository.findById(id)).thenReturn(Optional.of(existing));
        when(driverRepository.save(any(Driver.class))).thenReturn(updated);

        Driver result = driverService.updateDriver(id, 2.0, 3.0, false);

        assertThat(result.isAvailable()).isFalse();
        assertThat(result.getLocation()).isEqualTo(new Location(2.0, 3.0));
        verify(driverRepository).save(any(Driver.class));
    }

    @Test
    void updateDriver_shouldThrowWhenDriverDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(driverRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverService.updateDriver(id, 1.0, 2.0, true))
            .isInstanceOf(DriverNotFoundException.class);
    }

    @Test
    void getAvailableDrivers_shouldReturnNearestDriversWithDistance() {
        Driver driverOne = Driver.create(0.0, 0.0, true);
        Driver driverTwo = Driver.create(3.0, 4.0, true);
        when(driverRepository.findNearestAvailable(any(Location.class), eq(10)))
            .thenReturn(List.of(driverOne, driverTwo));

        List<DriverWithDistance> result = driverService.getAvailableDrivers(0.0, 0.0, 10);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).driver()).isEqualTo(driverOne);
        assertThat(result.get(0).distance()).isCloseTo(0.0, within(0.0001));
        assertThat(result.get(1).distance()).isCloseTo(5.0, within(0.0001));
    }
}
