package com.clhost.weatherbot.holder;

import com.clhost.weatherbot.entity.ForecastData;
import com.clhost.weatherbot.entity.Subscription;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Интерфейс, описывающий поведение держателей состояний
 */
public interface StatesHolder {
    /**
     * Загрузка состояний из стороннего источника
     */
    void load();

    /**
     *
     * @param subscription
     * @param weatherType
     */
    void addState(@NotNull final Subscription subscription, @NotNull final ForecastData.WeatherType weatherType);

    /**
     *
     * @param id
     * @param city
     * @return
     */
    boolean removeStateByIdAndCity(final long id, final String city);

    /**
     *
     * @return
     */
    Map<Subscription, ForecastData.WeatherType> getStates();
}
