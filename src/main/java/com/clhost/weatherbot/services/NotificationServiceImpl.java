package com.clhost.weatherbot.services;

import com.clhost.weatherbot.bot.MessageConstants;
import com.clhost.weatherbot.entity.ForecastData;
import com.clhost.weatherbot.entity.Subscription;
import com.clhost.weatherbot.holder.StatesHolder;
import com.clhost.weatherbot.utils.StringUtils;
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
            Thread.yield(); //fixme: мб удалить
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
                messageSender.send(subscription.getUserId(),String.format(
                        message,
                        subscription.getHours(),
                        StringUtils.getHourStringByHour(subscription.getHours())
                ));

                // заменить значение
                statesHolder.getStates().replace(
                        entry.getKey(),
                        entry.getValue(),
                        forecast.getWeather().getWeatherType());
                break;
            }
        }
    }

    /**
     * Разница между двумя датами в часах
     * @param a вычитаемое
     * @param b откуда вычитается
     * @return количество часов между двумя датами
     */
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
}
