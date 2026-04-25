package com.example.weatherapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.weatherapi.service.WeatherService;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/current/{city}")
    public ResponseEntity<?> getCurrentWeather(
            @PathVariable String city,
            @RequestParam(required = false) String unitGroup) {
        return ResponseEntity.ok(weatherService.getCurrentWeather(city, unitGroup));
    }

    @GetMapping("/forecast/{city}")
    public ResponseEntity<?> getForecast(
            @PathVariable String city,
            @RequestParam(required = false) String unitGroup,
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(weatherService.getForecast(city, unitGroup, days));
    }

    @GetMapping("/history/{city}")
    public ResponseEntity<?> getHistory(
            @PathVariable String city,
            @RequestParam String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String unitGroup) {
        return ResponseEntity.ok(weatherService.getHistory(city, startDate, endDate, unitGroup));
    }

    @GetMapping("/search/{city}")
    public ResponseEntity<?> searchCities(@PathVariable String city) {
        return ResponseEntity.ok(weatherService.searchCities(city));
    }
}
