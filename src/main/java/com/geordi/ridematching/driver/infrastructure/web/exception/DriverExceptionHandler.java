package com.geordi.ridematching.driver.infrastructure.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.geordi.ridematching.driver.application.exception.DriverNotFoundException;
import com.geordi.ridematching.shared.infrastructure.web.exception.ErrorResponse;

@RestControllerAdvice
public class DriverExceptionHandler {

    @ExceptionHandler(DriverNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDriverNotFoundException(DriverNotFoundException ex) {
        return new ErrorResponse("DRIVER_NOT_FOUND", ex.getMessage());
    }
}
