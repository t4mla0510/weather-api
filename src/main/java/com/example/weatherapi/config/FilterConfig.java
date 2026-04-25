package com.example.weatherapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.weatherapi.filter.RateLimitFilter;

@Configuration
public class FilterConfig {

    @Autowired
    private WeatherConfig weatherConfig;

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitFilter(weatherConfig));
        registrationBean.setUrlPatterns(java.util.Arrays.asList("/api/v1/weather/*"));
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
