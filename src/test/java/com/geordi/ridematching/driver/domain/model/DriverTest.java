package com.geordi.ridematching.driver.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.geordi.ridematching.shared.domain.model.Location;

class DriverTest {

    @Test
    void create_shouldCreateAvailableDriver() {
        Driver driver = Driver.create(10.0, 20.0, true);

        assertThat(driver.getId()).isNotNull();
        assertThat(driver.getLocation()).isEqualTo(new Location(10.0, 20.0));
        assertThat(driver.isAvailable()).isTrue();
    }

    @Test
    void create_shouldRejectNullAvailability() {
        assertThatThrownBy(() -> Driver.create(10.0, 20.0, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("available must not be null");
    }

    @Test
    void update_shouldReplaceLocationAndAvailability() {
        Driver driver = Driver.create(1.0, 2.0, true);

        Driver updated = driver.update(3.0, 4.0, false);

        assertThat(updated.getId()).isEqualTo(driver.getId());
        assertThat(updated.getLocation()).isEqualTo(new Location(3.0, 4.0));
        assertThat(updated.isAvailable()).isFalse();
        assertThat(driver.isAvailable()).isTrue();
    }

    @Test
    void markAsAvailable_and_markAsUnavailable_shouldReturnNewDriverInstance() {
        Driver driver = Driver.create(5.0, 6.0, true);

        Driver unavailable = driver.markAsUnavailable();
        Driver availableAgain = unavailable.markAsAvailable();

        assertThat(unavailable.getId()).isEqualTo(driver.getId());
        assertThat(unavailable.isAvailable()).isFalse();
        assertThat(availableAgain.isAvailable()).isTrue();
        assertThat(availableAgain).isNotSameAs(driver);
    }
}
