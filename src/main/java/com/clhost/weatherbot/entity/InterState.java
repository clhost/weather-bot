package com.clhost.weatherbot.entity;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "inter_state")
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class InterState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn
    private Subscription subscription;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "w_type", nullable = false)
    private ForecastData.WeatherType weatherType;

    public InterState(Subscription subscription, ForecastData.WeatherType weatherType) {
        this.subscription = subscription;
        this.weatherType = weatherType;
    }

    public InterState(long id, Subscription subscription, ForecastData.WeatherType weatherType) {
        this.id = id;
        this.subscription = subscription;
        this.weatherType = weatherType;
    }

    public InterState() {}

    public Subscription getSubscription() {
        return subscription;
    }

    public ForecastData.WeatherType getWeatherType() {
        return weatherType;
    }
}
