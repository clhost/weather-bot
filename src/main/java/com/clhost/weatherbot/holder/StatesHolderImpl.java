package com.clhost.weatherbot.holder;

import com.clhost.weatherbot.entity.ForecastData;
import com.clhost.weatherbot.entity.Subscription;
import com.clhost.weatherbot.repository.InterStateRepository;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatesHolderImpl implements StatesHolder {
    private static final Map<Subscription, ForecastData.WeatherType> states = new ConcurrentHashMap<>();
    private InterStateRepository interStateRepository;

    @Autowired
    public void setInterStateRepository(InterStateRepository interStateRepository) {
        this.interStateRepository = interStateRepository;
    }

    /**
     * Возврат состояния из базы в случае падения сервиса
     */
    @Override
    @PostConstruct
    public void load() {
        List<InterState> interStates = (List<InterState>) interStateRepository.findAll();
        for (InterState interState : interStates) {
            states.put(interState.getSubscription(), interState.getWeatherType());
        }
    }

    @Override
    public void addState(@NotNull final Subscription subscription,
                         @NotNull final ForecastData.WeatherType weatherType) {
        states.put(subscription, weatherType);
        interStateRepository.save(new InterState(subscription, weatherType));
    }

    @Override
    public boolean removeStateByIdAndCity(long id, String city) {
        return states.keySet().removeIf(s -> s.getUserId() == id && s.getCity().equalsIgnoreCase(city));
    }

    @Override
    public Map<Subscription, ForecastData.WeatherType> getStates() {
        return states;
    }

    @Entity
    @Table(name = "inter_state")
    @Setter
    @NoArgsConstructor
    public class InterState {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @OneToOne(cascade = CascadeType.MERGE)
        @PrimaryKeyJoinColumn
        private Subscription subscription;

        @Enumerated(EnumType.STRING)
        @Column(name = "w_type", nullable = false)
        private ForecastData.WeatherType weatherType;

        InterState(Subscription subscription, ForecastData.WeatherType weatherType) {
            this.subscription = subscription;
            this.weatherType = weatherType;
        }

        public Subscription getSubscription() {
            return subscription;
        }

        public ForecastData.WeatherType getWeatherType() {
            return weatherType;
        }
    }
}
