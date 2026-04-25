package com.example.weatherapi.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.weatherapi.cache.WeatherCache;
import com.example.weatherapi.config.WeatherConfig;
import com.example.weatherapi.exception.WeatherException;

import java.util.Map;
import java.util.HashMap;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;
    private final WeatherConfig weatherConfig;
    private final WeatherCache weatherCache;

    public WeatherService(RestTemplate restTemplate, WeatherConfig weatherConfig, WeatherCache weatherCache) {
        this.restTemplate = restTemplate;
        this.weatherConfig = weatherConfig;
        this.weatherCache = weatherCache;
    }

    public Map<String, Object> getCurrentWeather(String city, String unitGroup) {
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = weatherCache.get(city, Map.class);
        if (cached != null) {
            return cached;
        }
        Map<String, String> params = new HashMap<>();
        params.put("unitGroup", unitGroup != null ? unitGroup : "us");
        params.put("key", weatherConfig.getApiKey());
        Map<String, Object> result = fetchWeatherData("timeline/" + city, params);
        weatherCache.set(city, result);
        return result;
    }

    public Map<String, Object> getForecast(String city, String unitGroup, int days) {
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = weatherCache.get(city + "_forecast_" + days, Map.class);
        if (cached != null) {
            return cached;
        }
        Map<String, String> params = new HashMap<>();
        params.put("unitGroup", unitGroup != null ? unitGroup : "us");
        params.put("days", String.valueOf(days));
        params.put("key", weatherConfig.getApiKey());
        Map<String, Object> result = fetchWeatherData("timeline/" + city, params);
        weatherCache.set(city + "_forecast_" + days, result);
        return result;
    }

    public Map<String, Object> getHistory(String city, String startDate, String endDate, String unitGroup) {
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = weatherCache.get(city + "_history_" + startDate, Map.class);
        if (cached != null) {
            return cached;
        }
        Map<String, String> params = new HashMap<>();
        params.put("unitGroup", unitGroup != null ? unitGroup : "us");
        params.put("startDateTime", startDate);
        if (endDate != null) {
            params.put("endDateTime", endDate);
        }
        params.put("key", weatherConfig.getApiKey());
        Map<String, Object> result = fetchWeatherData("timeline/" + city, params);
        weatherCache.set(city + "_history_" + startDate, result);
        return result;
    }

    public Map<String, Object> searchCities(String city) {
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = weatherCache.get(city + "_search", Map.class);
        if (cached != null) {
            return cached;
        }
        Map<String, String> params = new HashMap<>();
        params.put("unitGroup", "us");
        params.put("key", weatherConfig.getApiKey());
        Map<String, Object> result = fetchWeatherData("timeline/" + city, params);
        weatherCache.set(city + "_search", result);
        return result;
    }

    private Map<String, Object> fetchWeatherData(String endpoint, Map<String, String> params) {
        try {
            String url = weatherConfig.getApiUrl() + endpoint + "?" + buildQueryString(params);
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>) restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new WeatherException("Failed to fetch weather data", HttpStatus.resolve(response.getStatusCode().value()));
            }
        } catch (RestClientException e) {
            throw new WeatherException("Failed to connect to weather service: " + e.getMessage());
        }
    }

    private String buildQueryString(Map<String, String> params) {
        return params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");
    }
}
