package com.clhost.weatherbot.entity;

import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "subscription")
@Setter
@ToString
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId; // id пользователя телеграм

    @Column(name = "hours", nullable = false)
    private int hours;

    @Column(name = "city", nullable = false)
    private String city;

    public Subscription(long userId, int hours, String city) {
        this.userId = userId;
        this.hours = hours;
        this.city = city;
    }

    public Subscription(long id, long userId, int hours, String city) {
        this.id = id;
        this.userId = userId;
        this.hours = hours;
        this.city = city;
    }

    public Subscription() {}

    public long getUserId() {
        return userId;
    }

    public int getHours() {
        return hours;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscription)) return false;

        Subscription that = (Subscription) o;

        if (userId != that.userId) return false;
        if (hours != that.hours) return false;
        return city != null ? city.equals(that.city) : that.city == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + hours;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }
}
