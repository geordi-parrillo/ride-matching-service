package com.geordi.ridematching.ride.infrastructure.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.geordi.ridematching.ride.application.exception.NoDriverAvailableException;
import com.geordi.ridematching.ride.application.exception.RideNotFoundException;
import com.geordi.ridematching.shared.infrastructure.web.exception.ErrorResponse;

@RestControllerAdvice
public class RideExceptionHandler {

    @ExceptionHandler(RideNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleRideNotFoundException(RideNotFoundException ex) {
        return new ErrorResponse("RIDE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(NoDriverAvailableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ErrorResponse handleNoDriverAvailable(NoDriverAvailableException ex) {
        return new ErrorResponse("NO_DRIVER_AVAILABLE", ex.getMessage());
    }
}
