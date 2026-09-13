package com.geordi.ridematching.ride.infrastructure.web.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record RideRequest(
    @NotNull UUID riderId,
    @NotNull Double x,
    @NotNull Double y) {
}
