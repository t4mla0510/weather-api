package com.example.weatherapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class WeatherExceptionHandler {

    @ExceptionHandler(WeatherException.class)
    public ResponseEntity<?> handleWeatherException(WeatherException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of(
                "error", e.getMessage(),
                "timestamp", System.currentTimeMillis()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error", "timestamp", System.currentTimeMillis()));
    }
}
