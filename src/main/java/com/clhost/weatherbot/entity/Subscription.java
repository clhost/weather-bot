package com.clhost.weatherbot.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "subscription")
@Setter
@NoArgsConstructor
@ToString
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "subscriber_id", nullable = false)
    private long subscriber; // id пользователя телеграм

    @Column(name = "hours", nullable = false)
    private int hours;

    @Column(name = "city", nullable = false)
    private String city;

    public Subscription(long subscriber, int hours, String city) {
        this.subscriber = subscriber;
        this.hours = hours;
        this.city = city;
    }

    public long getSubscriber() {
        return subscriber;
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

        if (subscriber != that.subscriber) return false;
        if (hours != that.hours) return false;
        return city != null ? city.equals(that.city) : that.city == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (subscriber ^ (subscriber >>> 32));
        result = 31 * result + hours;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }
}
