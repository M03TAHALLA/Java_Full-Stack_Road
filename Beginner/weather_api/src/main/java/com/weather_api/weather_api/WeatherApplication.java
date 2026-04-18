package com.weather_api.weather_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WeatherApplication {

    public static void main(String[] args) {

        SpringApplication.run(WeatherApplication.class, args);
    }

    // WHY: WeatherService needs an HTTP client object created by Spring.
    // WHAT: Registers RestTemplate as a Spring-managed object (bean).
    @Bean
    public RestTemplate restTemplate() {
        // WHY: This is the concrete HTTP client used to call Visual Crossing.
        // WHAT: Creates and returns a RestTemplate instance.
        return new RestTemplate();
    }
}
