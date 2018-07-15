package com.clhost.weatherbot.services;

import com.clhost.weatherbot.entity.ForecastData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.ApiContextInitializer;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ForecastServiceTest {

    @Autowired
    private ForecastService forecastService;
    private static final String CITY_ENG = "Saint Petersburg";

    static {
        // telegram context init
        ApiContextInitializer.init();
    }

    @Test
    public void testGetForecastByCityRus() throws Exception {
        // given
        String city = "Санкт-Петербург";

        // get forecast by city
        List<ForecastData> forecasts = forecastService.getForecastByCity(city);

        // assert
        // API возвращает город на английском языке, но принимает также русский
        for (ForecastData forecast : forecasts) {
            Assert.assertTrue(CITY_ENG.equalsIgnoreCase(forecast.getCity()));
        }
    }

    @Test
    public void testGetForecastByCityEng() throws Exception {
        // get forecast by city
        List<ForecastData> forecasts = forecastService.getForecastByCity(CITY_ENG);

        // assert
        for (ForecastData forecast : forecasts) {
            Assert.assertTrue(CITY_ENG.equalsIgnoreCase(forecast.getCity()));
        }
    }
}
