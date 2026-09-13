package com.geordi.ridematching.ride.application.exception;

public final class NoDriverAvailableException extends RuntimeException {

    public NoDriverAvailableException() {
        super("No available driver found for the requested pickup location");
    }
}
