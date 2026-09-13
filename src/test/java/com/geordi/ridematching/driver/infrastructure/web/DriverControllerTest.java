package com.geordi.ridematching.driver.infrastructure.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.geordi.ridematching.driver.application.DriverService;
import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.driver.domain.model.DriverWithDistance;
import com.geordi.ridematching.driver.infrastructure.web.dto.DriverRequest;
import com.geordi.ridematching.driver.infrastructure.web.dto.DriverResponse;

@ExtendWith(MockitoExtension.class)
class DriverControllerTest {

    @Mock
    private DriverService driverService;

    @InjectMocks
    private DriverController controller;

    @Test
    void create_shouldReturnDriverResponse() {
        DriverRequest request = new DriverRequest(5.0, 6.0, true);
        Driver driver = Driver.create(5.0, 6.0, true);
        when(driverService.createDriver(request.x(), request.y(), request.available())).thenReturn(driver);

        DriverResponse response = controller.create(request);

        assertThat(response.id()).isEqualTo(driver.getId());
        assertThat(response.x()).isEqualTo(5.0);
        assertThat(response.y()).isEqualTo(6.0);
        assertThat(response.distance()).isNull();
    }

    @Test
    void update_shouldReturnUpdatedDriverResponse() {
        UUID id = UUID.randomUUID();
        DriverRequest request = new DriverRequest(7.0, 8.0, false);
        Driver driver = Driver.create(7.0, 8.0, false);
        when(driverService.updateDriver(id, request.x(), request.y(), request.available())).thenReturn(driver);

        DriverResponse response = controller.update(id, request);

        assertThat(response.id()).isEqualTo(driver.getId());
        assertThat(response.x()).isEqualTo(7.0);
        assertThat(response.y()).isEqualTo(8.0);
        assertThat(response.distance()).isNull();
    }

    @Test
    void getAvailableDrivers_shouldReturnListOfResponses() {
        Driver driver = Driver.create(1.0, 2.0, true);
        DriverWithDistance driverWithDistance = new DriverWithDistance(driver, 4.5);
        when(driverService.getAvailableDrivers(0.0, 0.0, 10)).thenReturn(List.of(driverWithDistance));

        List<DriverResponse> response = controller.getAvailableDrivers(0.0, 0.0, 10);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).id()).isEqualTo(driver.getId());
        assertThat(response.get(0).distance()).isEqualTo(4.5);
    }
}
