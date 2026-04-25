# Weather API

A Spring Boot REST API for accessing weather data from Visual Crossing Weather API with Redis caching.

## API Endpoint

**Base URL**: `https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/{location}`

**Visual Crossing Weather API**: https://www.visualcrossing.com/weather-api

## API Endpoints

### Get Current Weather
```
GET /api/v1/weather/current/{city}
```

**Query Parameters**:
- `unitGroup` - (optional) `us` or `metric`

### Get Forecast
```
GET /api/v1/weather/forecast/{city}
```

**Query Parameters**:
- `unitGroup` - (optional) `us` or `metric`
- `days` - (default: 7) Number of forecast days

### Get Historical Weather
```
GET /api/v1/weather/history/{city}
```

**Query Parameters**:
- `startDate` - (required) Start date in format `YYYY-MM-DD`
- `endDate` - (optional) End date in format `YYYY-MM-DD`
- `unitGroup` - (optional) `us` or `metric`

### Search Cities
```
GET /api/v1/weather/search/{city}
```

## Configuration

### Using .env File

1. Copy `.env.example` to `.env` in the project root:

```bash
cp .env.example .env
```

2. Edit `.env` with your settings:

```env
WEATHER_API_KEY=your-api-key-here
REDIS_HOST=localhost
REDIS_PORT=6379
```

## Running the Application

### Prerequisites
- Java 21+
- Maven 3.8+
- Redis server (local or remote)

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

On Windows:
```cmd
mvnw.cmd spring-boot:run
```

## Features
- Visual Crossing Timeline API integration
- Redis caching with configurable TTL
- Auto-reload with spring-boot-devtools
- SpringDoc OpenAPI documentation at `/swagger-ui.html`

## API Keys & Security
- Get a free API key from https://www.visualcrossing.com/weather-api
- The free tier is rate-limited by IP address

## Development
- Spring Boot 4.0.6
- Java 21
- Redis for caching

## Roadmap
Project reference: https://roadmap.sh/projects/weather-api-wrapper-service
