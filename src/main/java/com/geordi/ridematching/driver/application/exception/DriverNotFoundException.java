package com.geordi.ridematching.driver.application.exception;

import java.util.UUID;

public final class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(UUID driverId) {
        super("Driver not found: " + driverId);
    }
}
