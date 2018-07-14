package com.clhost.weatherbot.services;

import com.clhost.weatherbot.bot.WeatherBot;
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

    @Autowired
    public void setBot(WeatherBot bot) {
        this.bot = bot;
    }

    @Override
    public void send(Long userId, String message) {
        Long chatId = usersChats.get(userId);
        try {
            // todo: log here
            bot.execute(new SendMessage(chatId, message));
            System.out.println("Отправлено: " + message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(long userId, long chatId) {
        usersChats.put(userId, chatId);
    }
}
