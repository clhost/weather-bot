package com.clhost.weatherbot.entity;

public class MainData {
    private long id;
    private double temp;
    private double tempMin;
    private double tempMax;
    private double pressure;
    private double seaLevelPressure;
    private double groundLevelPressure;
    private double humidity;

    public MainData(double temp, double tempMin, double tempMax, double pressure,
                    double seaLevelPressure, double groundLevelPressure, double humidity) {
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.seaLevelPressure = seaLevelPressure;
        this.groundLevelPressure = groundLevelPressure;
        this.humidity = humidity;
    }

    public double getTemp() {
        return temp;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public double getPressure() {
        return pressure;
    }

    public double getSeaLevelPressure() {
        return seaLevelPressure;
    }

    public double getGroundLevelPressure() {
        return groundLevelPressure;
    }

    public double getHumidity() {
        return humidity;
    }

    @Override
    public String toString() {
        return "MainData{" +
                "temp=" + temp +
                ", tempMin=" + tempMin +
                ", tempMax=" + tempMax +
                ", pressure=" + pressure +
                ", seaLevelPressure=" + seaLevelPressure +
                ", groundLevelPressure=" + groundLevelPressure +
                ", humidity=" + humidity +
                '}';
    }
}
