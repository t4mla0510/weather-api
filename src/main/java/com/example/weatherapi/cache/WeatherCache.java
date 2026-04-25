package com.example.weatherapi.cache;

import com.example.weatherapi.config.WeatherConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class WeatherCache {

    private static final Logger logger = LoggerFactory.getLogger(WeatherCache.class);

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final WeatherConfig weatherConfig;

    private static final String CACHE_PREFIX = "weather:";

    public WeatherCache(RedisTemplate<String, Object> redisTemplate, 
                        ObjectMapper objectMapper,
                        WeatherConfig weatherConfig) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.weatherConfig = weatherConfig;
        
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new StringRedisSerializer());
    }

    public <T> T get(String city, Class<T> clazz) {
        String key = CACHE_PREFIX + city;
        logger.debug("Attempting to get cached data for city: {}", city);
        Object value = redisTemplate.opsForValue().get(key);
        
        if (value == null) {
            logger.debug("No cache found for city: {}", city);
            return null;
        }
        
        logger.debug("Cache hit for city: {}", city);
        try {
            String jsonString = String.valueOf(value);
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            logger.error("Failed to deserialize cached data for city: {}", city, e);
            return null;
        }
    }

    public <T> T get(String city, com.fasterxml.jackson.core.type.TypeReference<T> typeRef) {
        String key = CACHE_PREFIX + city;
        Object value = redisTemplate.opsForValue().get(key);
        
        if (value == null) {
            return null;
        }
        
        try {
            String jsonString = String.valueOf(value);
            return objectMapper.readValue(jsonString, typeRef);
        } catch (Exception e) {
            return null;
        }
    }

    public void set(String city, Object data) {
        String key = CACHE_PREFIX + city;
        logger.debug("Caching data for city: {}", city);
        try {
            String jsonString = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, jsonString, weatherConfig.getCacheTtl(), TimeUnit.SECONDS);
            logger.debug("Successfully cached data for city: {} (key: {})", city, key);
        } catch (Exception e) {
            logger.error("Failed to cache data for city: {}", city, e);
            throw new RuntimeException("Failed to cache data for city: " + city, e);
        }
    }

    public void delete(String city) {
        String key = CACHE_PREFIX + city;
        redisTemplate.delete(key);
    }

    public boolean exists(String city) {
        String key = CACHE_PREFIX + city;
        return redisTemplate.hasKey(key);
    }

    public void invalidateAll() {
        String pattern = CACHE_PREFIX + "*";
        redisTemplate.delete(redisTemplate.keys(pattern));
    }
}
