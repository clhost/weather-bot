package com.clhost.weatherbot.config;

import com.clhost.weatherbot.bot.WeatherBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.TelegramBotsApi;

@Configuration
public class BotApiBindConfig implements CommandLineRunner {
    private TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

    @Autowired
    private WeatherBot weatherBot;

    @Override
    public void run(String... args) throws Exception {
        telegramBotsApi.registerBot(weatherBot);
    }
}
