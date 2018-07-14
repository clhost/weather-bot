package com.clhost.weatherbot.entity;

public class Wind {
    private long id;
    private double speed;
    private double degree;

    public Wind(double speed, double degree) {
        this.speed = speed;
        this.degree = degree;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDegree() {
        return degree;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                ", degree=" + degree +
                '}';
    }
}