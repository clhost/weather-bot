package com.clhost.weatherbot.services;


import com.clhost.weatherbot.entity.Subscription;
import org.jetbrains.annotations.NotNull;

public interface SubscribeService {
    /**
     *
     * @param subscription представление подписки {@link Subscription}
     */
    void subscribe(@NotNull final Subscription subscription);

    /**
     * Так как пользователь может иметь более одной подписки (за счет города), то целесообразно передавать в метод
     * идентификатор пользователя и город.
     * @param userId идентификатор пользователя
     * @param city город, от обновлений погоды которого следует отписаться
     * @return
     */
    boolean unsubscribe(final long userId, @NotNull final String city);
}
