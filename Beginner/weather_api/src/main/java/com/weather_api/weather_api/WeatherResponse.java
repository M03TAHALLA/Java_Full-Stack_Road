package com.weather_api.weather_api;


public class WeatherResponse {
    private String city;
    private double temperatureCelsius;


    private String condition;

    public WeatherResponse(String city, double temperatureCelsius, String condition) {
        this.city = city;
        this.temperatureCelsius = temperatureCelsius;
        this.condition = condition;
    }


    public String getCity() {
        return city;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public String getCondition() {
        return condition;
    }
}
