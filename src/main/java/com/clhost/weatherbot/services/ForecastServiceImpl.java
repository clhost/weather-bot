package com.clhost.weatherbot.services;

import com.clhost.weatherbot.cache.SimpleCache;
import com.clhost.weatherbot.entity.ForecastData;
import com.clhost.weatherbot.entity.InternalWeather;
import com.clhost.weatherbot.entity.MainData;
import com.clhost.weatherbot.entity.Wind;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.*;
import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;


@Service
public class ForecastServiceImpl implements ForecastService {
    @Value("${weather.app.token}")
    private String apiKey;

    private OWM owm;
    private SimpleCache cache;

    @PostConstruct
    private void configureOwm() {
        owm = new OWM(apiKey);
    }

    @Autowired
    public void setCache(SimpleCache cache) {
        this.cache = cache;
    }

    @Override
    public List<ForecastData> getForecastByCity(@NotNull String city) {
        List<ForecastData> forecastData = cache.getByCity(city);
        if (forecastData == null) {
            try {
                HourlyWeatherForecast dataList = owm.hourlyWeatherForecastByCityName(city);
                forecastData = new ArrayList<>();
                if (dataList.getDataList() != null) {
                    for (WeatherData weatherData : dataList.getDataList()) {
                        forecastData.add(map(weatherData));
                    }
                }
                cache.add(city, forecastData);
            } catch (APIException e) {
                e.printStackTrace();
            }
        }

        return forecastData;
    }

    private ForecastData map(WeatherData weatherData) {
        Date d;
        Long date = (d = weatherData.getDateTime()) == null ? 0L : d.getTime();
        Cloud c;
        Double cloud = (c = weatherData.getCloudData()) == null ? 0d : c.getCloud();

        InternalWeather internalWeather = null;
        if (weatherData.getWeatherList() != null) {
            internalWeather = mapWeather(weatherData.getWeatherList().get(0));
        }

        String dateTimeText = weatherData.getDateTimeText();
        return new ForecastData(
                date,
                mapMainData(weatherData.getMainData()),
                internalWeather,
                mapWind(weatherData.getWindData()),
                cloud,
                dateTimeText == null ? null :
                        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(dateTimeText)
        );
    }

    private Wind mapWind(net.aksingh.owmjapis.model.param.Wind wind) {
        Optional<Double> speed = Optional.ofNullable(wind.getSpeed());
        Optional<Double> degree = Optional.ofNullable(wind.getDegree());

        return new Wind(speed.orElse(0d), degree.orElse(0d));
    }

    private MainData mapMainData(Main main) {
        Optional<Double> temp = Optional.ofNullable(main.getTemp());
        Optional<Double> tempMin = Optional.ofNullable(main.getTempMin());
        Optional<Double> tempMax = Optional.ofNullable(main.getTempMax());
        Optional<Double> pressure = Optional.ofNullable(main.getPressure());
        Optional<Double> seaLevel = Optional.ofNullable(main.getSeaLevel());
        Optional<Double> groundLevel = Optional.ofNullable(main.getGroundLevel());
        Optional<Double> humidity = Optional.ofNullable(main.getHumidity());

        return new MainData(
                temp.orElse(0d),
                tempMin.orElse(0d),
                tempMax.orElse(0d),
                pressure.orElse(0d),
                seaLevel.orElse(0d),
                groundLevel.orElse(0d),
                humidity.orElse(0d)
        );
    }

    private InternalWeather mapWeather(Weather weather) {
        ForecastData.WeatherType wType = ForecastData.WeatherType.getByDesc(weather.getMainInfo());
        String desc = weather.getDescription();

        return new InternalWeather(wType, desc);
    }
}
