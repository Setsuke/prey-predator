package com.ecosystemsandbox.common.api;

import com.ecosystemsandbox.simulation.service.SimulationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SimulationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleSimulationNotFound(SimulationNotFoundException exception) {
        return ApiErrorResponse.of(HttpStatus.NOT_FOUND.value(), "Not Found", exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleValidationError(MethodArgumentNotValidException exception) {
        return ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Simulation parameters are invalid.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleIllegalArgument(IllegalArgumentException exception) {
        return ApiErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "Bad Request", exception.getMessage());
    }
}
