package com.clhost.weatherbot.services;

public interface MessageSender {
    /**
     *
     * @param userId
     * @param message
     */
    void send(Long userId, String message);

    /**
     *
     * @param userId
     * @param chatId
     */
    void register(final long userId, final long chatId);
}
