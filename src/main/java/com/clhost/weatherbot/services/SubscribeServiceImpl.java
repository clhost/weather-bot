package com.clhost.weatherbot.services;

import com.clhost.weatherbot.entity.ForecastData;
import com.clhost.weatherbot.entity.Subscription;
import com.clhost.weatherbot.holder.StatesHolder;
import com.clhost.weatherbot.repository.SubscriptionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubscribeServiceImpl implements SubscribeService {
    private SubscriptionRepository repository;
    private StatesHolder statesHolder;

    @Autowired
    public void setSubscribeServiceImpl(SubscriptionRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setStatesHolder(StatesHolder statesHolder) {
        this.statesHolder = statesHolder;
    }

    @Override
    public void subscribe(@NotNull Subscription subscription) {
        repository.save(subscription);
        statesHolder.addState(subscription, ForecastData.WeatherType.DEFAULT);
    }

    @Override
    public boolean unsubscribe(long userId, @NotNull String city) {
        repository.deleteByUserIdAndCity(userId, city);
        return statesHolder.removeStateByIdAndCity(userId, city);
    }

    @Override
    public List<Subscription> showSubscriptions(long userId) {
        return repository.findAllByUserId(userId);
    }
}
