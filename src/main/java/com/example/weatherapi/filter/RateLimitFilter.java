package com.example.weatherapi.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import com.example.weatherapi.config.WeatherConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
public class RateLimitFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, Integer> requestCounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> requestTimestamps = new ConcurrentHashMap<>();
    
    private int rateLimit = 100;

    public RateLimitFilter(WeatherConfig weatherConfig) {
        this.rateLimit = weatherConfig.getRateLimit();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) 
            throws ServletException, IOException {
        
        String clientIp = request.getRemoteAddr();
        String path = request.getRequestURI();
        
        if (path.startsWith("/api/v1/weather")) {
            String rateLimitKey = clientIp + ":" + path;
            
            long now = System.currentTimeMillis();
            long windowStart = requestTimestamps.computeIfAbsent(rateLimitKey, k -> now);
            
            if (now - windowStart > 60000) {
                requestTimestamps.put(rateLimitKey, now);
                requestCounts.put(rateLimitKey, 1);
            } else {
                int count = requestCounts.computeIfAbsent(rateLimitKey, k -> 0);
                if (count >= rateLimit) {
                    response.setStatus(429);
                    response.setContentType("application/json");
                    response.getWriter().write(
                        "{\"error\": \"Rate limit exceeded\", \"retryAfter\": 60}"
                    );
                    return;
                }
                requestCounts.put(rateLimitKey, count + 1);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
