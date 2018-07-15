package com.clhost.weatherbot.holder;

import com.clhost.weatherbot.entity.ForecastData;
import com.clhost.weatherbot.entity.InterState;
import com.clhost.weatherbot.entity.Subscription;
import com.clhost.weatherbot.logger.Logging;
import com.clhost.weatherbot.repository.InterStateRepository;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatesHolderImpl implements StatesHolder {
    private static final Map<Subscription, ForecastData.WeatherType> states = new ConcurrentHashMap<>();
    private InterStateRepository interStateRepository;

    @Logging
    private Logger logger;

    public StatesHolderImpl() {}

    @Autowired
    public void setInterStateRepository(InterStateRepository interStateRepository) {
        this.interStateRepository = interStateRepository;
    }

    /*
     * Возврат состояния из базы в случае падения сервиса
     */
    @Override
    @PostConstruct
    public void load() {
        List<InterState> interStates = (List<InterState>) interStateRepository.findAll();
        for (InterState interState : interStates) {
            states.put(interState.getSubscription(), interState.getWeatherType());
        }
        logger.info("Subscription's states has been loaded from persistent storage.");
    }

    @Override
    public void addState(@NotNull final Subscription subscription,
                         @NotNull final ForecastData.WeatherType weatherType) {
        states.put(subscription, weatherType);
        interStateRepository.save(new InterState(subscription, weatherType));
    }

    @Override
    public boolean removeStateByUserIdAndCity(long userId, String city) {
        return states.keySet().removeIf(s -> s.getUserId() == userId && s.getCity().equalsIgnoreCase(city));
    }

    @Override
    public Map<Subscription, ForecastData.WeatherType> getStates() {
        return states;
    }
}
