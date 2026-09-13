package com.geordi.ridematching.shared.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void create_shouldCreateLocation() {
        Location location = Location.create(3.0, 4.0);

        assertThat(location.x()).isEqualTo(3.0);
        assertThat(location.y()).isEqualTo(4.0);
    }

    @Test
    void create_shouldRejectNullCoordinates() {
        assertThatThrownBy(() -> Location.create(null, 4.0))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> Location.create(3.0, null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void distanceTo_shouldCalculateEuclideanDistance() {
        Location first = Location.create(0.0, 0.0);
        Location second = Location.create(3.0, 4.0);

        assertThat(first.distanceTo(second)).isEqualTo(5.0);
    }
}
