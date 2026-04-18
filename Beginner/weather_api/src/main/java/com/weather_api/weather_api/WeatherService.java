package com.weather_api.weather_api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class WeatherService {


    private final RestTemplate restTemplate;

    // WHY: Like a Env file we find it in ressources/application.properties
    @Value("${weather.api.key:}")
    private String apiKey;

    @Value("${weather.api.base-url:}")
    private String baseUrl;

    // WHY: Constructor injection makes dependencies.
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // WHY: Controller needs one simple method to get live weather for a city.
    // WHAT: Validates input, calls Visual Crossing, then converts response into WeatherResponse.
    public WeatherResponse getWeatherForCity(String city) {
        String cleanCity = city == null ? "" : city.trim();

        if (cleanCity.isEmpty()) {
            throw new IllegalArgumentException("City query parameter is required.");
        }

        if (apiKey.isBlank() || "YOUR_VISUAL_CROSSING_API_KEY".equals(apiKey)) {
            throw new IllegalStateException("Set weather.api.key in application.properties before calling /weather.");
        }

        // WHY: Building URLs safely prevents mistakes with spaces/special characters.
        // WHAT: Creates the final Visual Crossing URL with query parameters.
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .pathSegment(cleanCity)
                .queryParam("unitGroup", "metric")
                .queryParam("key", apiKey)
                .queryParam("contentType", "json")
                .build()
                .toUriString();

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null) {
            throw new IllegalStateException("Weather API returned an empty response.");
        }

        // WHAT: Reads nested object currentConditions from the API JSON.
        @SuppressWarnings("unchecked")
        Map<String, Object> currentConditions = (Map<String, Object>) response.get("currentConditions");

        if (currentConditions == null) {
            throw new IllegalStateException("Weather API response missing currentConditions.");
        }

        String resolvedCity = String.valueOf(response.getOrDefault("resolvedAddress", cleanCity));

        Object temperatureValue = currentConditions.get("temp");
        double temperatureCelsius = temperatureValue instanceof Number
                ? ((Number) temperatureValue).doubleValue()
                : Double.parseDouble(String.valueOf(temperatureValue));
        String condition = String.valueOf(currentConditions.getOrDefault("conditions", "Unknown"));

        // WHAT: Returns the final object Spring will serialize as JSON.
        return new WeatherResponse(resolvedCity, temperatureCelsius, condition);
    }
}
