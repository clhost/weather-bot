package com.clhost.weatherbot.services;

import com.clhost.weatherbot.bot.WeatherBot;
import com.clhost.weatherbot.logger.Logging;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageSenderImpl implements MessageSender {
    private static final Map<Long, Long> usersChats = new ConcurrentHashMap<>();
    private WeatherBot bot;

    @Logging
    private Logger logger;

    @Autowired
    public void setBot(WeatherBot bot) {
        this.bot = bot;
    }

    @Override
    public void send(long userId, @NotNull String message) {
        Long chatId = usersChats.get(userId);
        try {
            bot.execute(new SendMessage(chatId, message));
            logger.info("Sent message to chat [id:" + chatId + "]: " + message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(long userId, long chatId) {
        if (!usersChats.containsKey(userId)) {
            usersChats.put(userId, chatId);
            logger.info("New user [id:" + userId + "] has been registered.");
        }
    }
}
