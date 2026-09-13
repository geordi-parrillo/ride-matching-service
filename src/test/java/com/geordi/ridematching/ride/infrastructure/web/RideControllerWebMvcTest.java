package com.geordi.ridematching.ride.infrastructure.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.geordi.ridematching.ride.application.RideService;
import com.geordi.ridematching.ride.domain.model.Ride;
import com.geordi.ridematching.ride.infrastructure.web.dto.RideRequest;
import com.geordi.ridematching.ride.infrastructure.web.exception.RideExceptionHandler;

import tools.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = RideController.class)
@Import(RideExceptionHandler.class)
class RideControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RideService rideService;

    @Test
    void createRide_shouldReturnCreatedRideResponse() throws Exception {
        UUID riderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();
        Ride ride = Ride.create(riderId, driverId, 10.0, 20.0);
        given(rideService.create(riderId, 10.0, 20.0)).willReturn(ride);

        mockMvc.perform(post("/api/rides")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RideRequest(riderId, 10.0, 20.0))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(ride.getId().toString()))
            .andExpect(jsonPath("$.riderId").value(riderId.toString()))
            .andExpect(jsonPath("$.driverId").value(driverId.toString()))
            .andExpect(jsonPath("$.pickupX").value(10.0))
            .andExpect(jsonPath("$.pickupY").value(20.0))
            .andExpect(jsonPath("$.status").value("ONGOING"));
    }

    @Test
    void completeRide_shouldReturnCompletedRideResponse() throws Exception {
        UUID rideId = UUID.randomUUID();
        UUID riderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();
        Ride ride = Ride.create(riderId, driverId, 5.0, 6.0).complete();
        given(rideService.markAsComplete(rideId)).willReturn(ride);

        mockMvc.perform(post("/api/rides/{id}/complete", rideId)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(ride.getId().toString()))
            .andExpect(jsonPath("$.riderId").value(riderId.toString()))
            .andExpect(jsonPath("$.driverId").value(driverId.toString()))
            .andExpect(jsonPath("$.pickupX").value(5.0))
            .andExpect(jsonPath("$.pickupY").value(6.0))
            .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}
