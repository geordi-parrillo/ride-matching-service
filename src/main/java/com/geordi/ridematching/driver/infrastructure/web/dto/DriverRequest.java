package com.geordi.ridematching.driver.infrastructure.web.dto;

import jakarta.validation.constraints.NotNull;

public record DriverRequest(
		@NotNull Double x,
		@NotNull Double y,
		@NotNull Boolean available) {
}