package com.clhost.weatherbot.services;

import com.clhost.weatherbot.entity.ForecastData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ForecastService {
    /**
     *
     * @param city город, прогноз погоды которого требуется
     * @return list of {@link ForecastData}
     */
    List<ForecastData> getForecastByCity(@NotNull final String city);
}
