package com.clhost.weatherbot.entity;

import org.joda.time.DateTime;


public class ForecastData {
    private long id;
    private long unixTime;
    private MainData mainData;
    private InternalWeather weather;
    private Wind wind;
    private double clouds;
    private DateTime dateTime;

    public ForecastData(long unixTime, MainData mainData, InternalWeather weather, Wind wind,
                        double clouds, DateTime dateTime) {
        this.unixTime = unixTime;
        this.mainData = mainData;
        this.weather = weather;
        this.wind = wind;
        this.clouds = clouds;
        this.dateTime = dateTime;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public MainData getMainData() {
        return mainData;
    }

    public InternalWeather getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public double getClouds() {
        return clouds;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public enum WeatherType {
        THUNDERSTORM("thunderstorm"),
        DRIZZLE("drizzle"),
        RAIN("rain"),
        SNOW("snow"),
        ATMOSPHERE("atmosphere"),
        CLEAR("clear"),
        CLOUDS("clouds"),
        DEFAULT("default");

        private final String desc;
        WeatherType(String desc) {
            this.desc = desc;
        }

        public static WeatherType getByDesc(String desc) {
            if (desc.equalsIgnoreCase(THUNDERSTORM.desc)) return THUNDERSTORM;
            if (desc.equalsIgnoreCase(DRIZZLE.desc)) return DRIZZLE;
            if (desc.equalsIgnoreCase(RAIN.desc)) return RAIN;
            if (desc.equalsIgnoreCase(SNOW.desc)) return SNOW;
            if (desc.equalsIgnoreCase(ATMOSPHERE.desc)) return ATMOSPHERE;
            if (desc.equalsIgnoreCase(CLEAR.desc)) return CLEAR;
            if (desc.equalsIgnoreCase(CLOUDS.desc)) return CLOUDS;
            return null;
        }

        @Override
        public String toString() {
            return desc;
        }
    }

    @Override
    public String toString() {
        return "ForecastData{" +
                "unixTime=" + unixTime +
                ", mainData=" + mainData +
                ", weather=" + weather +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", dateTime=" + dateTime +
                '}';
    }
}
