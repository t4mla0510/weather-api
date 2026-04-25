# Weather API Dependencies

## Core Dependencies
- **spring-boot-starter-webmvc**: Web framework for building REST APIs
- **spring-boot-starter-data-redis**: Redis client for caching
- **springdoc-openapi-starter-webmvc-ui**: OpenAPI/Swagger for API documentation

## Build Dependencies
- **lombok**:_reduce boilerplate code_
- **spring-boot-devtools**: Hot reload for development

## External Integrations
- **Visual Crossing Weather API**: Free weather data service
- **Redis**: In-memory caching server

## Required Services
1. **Redis Server** (default: localhost:6379)
   - For caching weather data with TTL
   
2. **Visual Crossing API Key**
   - Get free key at: https://www.visualcrossing.com/weather-api
   - Set via `WEATHER_API_KEY` environment variable

## API Features
- Current weather lookup
- Forecast (up to 15 days)
- Historical weather data
- City search
- Rate limiting (100 requests/minute per IP)
- Redis caching (12-hour TTL)

## Environment Variables
| Variable | Default | Description |
|----------|---------|-------------|
| WEATHER_API_KEY | - | Your Visual Crossing API key (required) |
| REDIS_HOST | localhost | Redis server host |
| REDIS_PORT | 6379 | Redis server port |
| CACHE_TTL | 43200 | Cache TTL in seconds (12 hours) |
| PORT | 8080 | Application port |
