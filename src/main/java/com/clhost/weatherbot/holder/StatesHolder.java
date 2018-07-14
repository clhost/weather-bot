package com.clhost.weatherbot.holder;

import com.clhost.weatherbot.entity.ForecastData;
import com.clhost.weatherbot.entity.Subscription;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Интерфейс, описывающий поведение держателей состояний.
 * Держатель состояний - сервис, отвечающий за поддержку состояний погоды для конкретной подписки.
 */
public interface StatesHolder {
    /**
     * Загрузка состояний из стороннего источника
     */
    void load();

    /**
     * Добавить состояние
     * @param subscription подписка
     * @param weatherType тип погоды
     */
    void addState(@NotNull final Subscription subscription, @NotNull final ForecastData.WeatherType weatherType);

    /**
     *
     * @param userId идентификатор пользователя
     * @param city город
     * @return true, если удаление произошло успешно, false, если произошла ошибка или не было такой записи
     */
    boolean removeStateByUserIdAndCity(final long userId, final String city);

    /**
     *
     * @return все состояния
     */
    Map<Subscription, ForecastData.WeatherType> getStates();
}
