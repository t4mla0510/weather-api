package com.example.weatherapi.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record WeatherData(
        StringQuery queryTime,
        List<ResultsValue> values
) {
    public static record StringQuery(String address, String units, String days) {}
    
    public static record ResultsValue(
            String _provider,
            String _type,
            String _name,
            String _id,
            String _unitGroup,
            LocalDate datetime,
            Double temp,
            Double feelslike,
            Double humidity,
            Double windspeed,
            Double winddir,
            Double pressure,
            Double visibility,
            Double cloudcover,
            Double solarradiation,
            Double precipitation,
            Double snow,
            Double snowdepth,
            String icon,
            List<String> conditions,
            Map<String, Object> stations
    ) {}
}
