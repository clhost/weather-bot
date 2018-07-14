package com.clhost.weatherbot.services;

import org.jetbrains.annotations.NotNull;

public interface MessageSender {
    /**
     *
     * @param userId идентификатор пользователя
     * @param message отправляемое сообщение
     */
    void send(final long userId, @NotNull final String message);

    /**
     *
     * @param userId идентификатор пользователя
     * @param chatId идентификатор чата, связанного с userId
     */
    void register(final long userId, final long chatId);
}
