package com.clhost.weatherbot.services;

import com.clhost.weatherbot.bot.MessageConstants;
import com.clhost.weatherbot.entity.ForecastData;
import com.clhost.weatherbot.entity.Subscription;
import com.clhost.weatherbot.holder.StatesHolder;
import com.clhost.weatherbot.logger.Logging;
import com.clhost.weatherbot.utils.StringUtils;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class NotificationServiceImpl implements NotificationService {
    private MessageSender messageSender;
    private StatesHolder statesHolder;
    private ForecastService forecastService;
    private AtomicBoolean isInterrupted = new AtomicBoolean(false);

    @Logging
    private Logger logger;

    @Autowired
    public void setStatesHolder(StatesHolder statesHolder) {
        this.statesHolder = statesHolder;
    }

    @Autowired
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Autowired
    public void setForecastService(ForecastService forecastService) {
        this.forecastService = forecastService;
    }

    @Override
    public void loop() {
        while (!isInterrupted.get()) {
            Map<Subscription, ForecastData.WeatherType> states = statesHolder.getStates();
            for (Map.Entry<Subscription, ForecastData.WeatherType> entry : states.entrySet()) {
                forecastLoop(entry);
            }
            Thread.yield();
        }
    }

    @Override
    public void interrupt() {
        isInterrupted.compareAndSet(false, true);
    }

    private void forecastLoop(Map.Entry<Subscription, ForecastData.WeatherType> entry) {
        Subscription subscription = entry.getKey();
        String city = subscription.getCity();
        List<ForecastData> forecasts = forecastService.getForecastByCity(city);

        for (ForecastData forecast : forecasts) {
            // есть изменение погоды
            if (entry.getValue() != forecast.getWeather().getWeatherType() &&
                    hoursBetweenDates(DateTime.now(), forecast.getDateTime()) == subscription.getHours()) {
                String message = getMessageByWeatherType(forecast.getWeather().getWeatherType());
                messageSender.send(subscription.getUserId(),
                        fullWeather(
                                String.format(
                                        message,
                                        subscription.getHours(),
                                        StringUtils.getHourStringByHour(subscription.getHours())),
                                forecast
                        )
                );

                // заменить значение состояния
                statesHolder.getStates().replace(
                        entry.getKey(),
                        entry.getValue(),
                        forecast.getWeather().getWeatherType());
                logger.info("Notification has been sent for user [id:" + subscription.getUserId() + "] " +
                        "for city " + subscription.getCity());
                break;
            }
        }
    }

    // разница между датами в часах
    private int hoursBetweenDates(DateTime a, DateTime b) {
        return (int) ((b.getMillis() - a.getMillis()) / (60 * 60 * 1000));
    }

    private String getMessageByWeatherType(ForecastData.WeatherType type) {
        switch (type) {
            case THUNDERSTORM: return MessageConstants.THUNDERSTORM;
            case DRIZZLE: return MessageConstants.DRIZZLE;
            case RAIN: return MessageConstants.RAIN;
            case SNOW: return MessageConstants.SNOW;
            case ATMOSPHERE: return MessageConstants.ATMOSPHERE;
            case CLEAR: return MessageConstants.CLEAR;
            case CLOUDS: return MessageConstants.CLOUDS;
        }
        return null;
    }

    private String fullWeather(String message, ForecastData forecast) {
        return message + "\nКраткая информация по ожидаемой погоде:\n" +
                "Температура: " + forecast.getMainData().getTemp() + "К.\n" +
                "Давление: " + forecast.getMainData().getPressure() + "Па.\n" +
                "Влажность: " + forecast.getMainData().getHumidity() + "%.\n" +
                "Облачность: " + forecast.getClouds() + "%.\n" +
                "Скорость ветра: " + forecast.getWind().getSpeed() + "м/c.";
    }
}
