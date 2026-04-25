package com.example.weatherapi.exception;

import org.springframework.http.HttpStatus;

public class WeatherException extends RuntimeException {

    private final HttpStatus status;

    public WeatherException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public WeatherException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
