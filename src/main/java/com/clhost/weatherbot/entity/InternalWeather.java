package com.clhost.weatherbot.entity;

public class InternalWeather {
    private ForecastData.WeatherType weatherType;
    private String description;

    public InternalWeather(ForecastData.WeatherType weatherType, String description) {
        this.weatherType = weatherType;
        this.description = description;
    }

    public ForecastData.WeatherType getWeatherType() {
        return weatherType;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "InternalWeather{" +
                "weatherType=" + weatherType +
                ", description='" + description + '\'' +
                '}';
    }
}
