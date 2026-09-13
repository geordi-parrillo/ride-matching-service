package com.geordi.ridematching.driver.infrastructure.web;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.geordi.ridematching.driver.application.DriverService;
import com.geordi.ridematching.driver.domain.model.Driver;
import com.geordi.ridematching.driver.domain.model.DriverWithDistance;
import com.geordi.ridematching.driver.infrastructure.web.dto.DriverRequest;
import com.geordi.ridematching.driver.infrastructure.web.exception.DriverExceptionHandler;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = DriverController.class)
@Import(DriverExceptionHandler.class)
class DriverControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DriverService driverService;

    @Test
    void createDriver_shouldReturnCreatedDriverResponse() throws Exception {
        Driver driver = Driver.create(10.0, 20.0, true);
        given(driverService.createDriver(10.0, 20.0, true)).willReturn(driver);

        mockMvc.perform(post("/api/drivers")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DriverRequest(10.0, 20.0, true))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(driver.getId().toString()))
            .andExpect(jsonPath("$.x").value(10.0))
            .andExpect(jsonPath("$.y").value(20.0))
            .andExpect(jsonPath("$.distance").value(nullValue()));
    }

    @Test
    void updateDriver_shouldReturnUpdatedDriverResponse() throws Exception {
        UUID driverId = UUID.randomUUID();
        Driver driver = Driver.create(5.0, 6.0, false);
        given(driverService.updateDriver(eq(driverId), eq(5.0), eq(6.0), eq(false))).willReturn(driver);

        mockMvc.perform(put("/api/drivers/{id}", driverId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DriverRequest(5.0, 6.0, false))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(driver.getId().toString()))
            .andExpect(jsonPath("$.x").value(5.0))
            .andExpect(jsonPath("$.y").value(6.0))
            .andExpect(jsonPath("$.distance").value(nullValue()));
    }

    @Test
    void getAvailableDrivers_shouldReturnNearestDrivers() throws Exception {
        Driver driver = Driver.create(1.0, 2.0, true);
        given(driverService.getAvailableDrivers(0.0, 0.0, 10)).willReturn(List.of(new DriverWithDistance(driver, 2.5)));

        mockMvc.perform(get("/api/drivers/available")
                .param("x", "0.0")
                .param("y", "0.0")
                .param("limit", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(driver.getId().toString()))
            .andExpect(jsonPath("$[0].x").value(1.0))
            .andExpect(jsonPath("$[0].y").value(2.0))
            .andExpect(jsonPath("$[0].distance").value(2.5));
    }
}
