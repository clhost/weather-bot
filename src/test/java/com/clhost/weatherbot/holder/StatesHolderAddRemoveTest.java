package com.clhost.weatherbot.holder;

import com.clhost.weatherbot.entity.ForecastData;
import com.clhost.weatherbot.entity.Subscription;
import com.clhost.weatherbot.repository.InterStateRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.ApiContextInitializer;

@ActiveProfiles("mock_interstate_repository")
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"classpath:subscription_insert.sql", "classpath:interstate_insert.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatesHolderAddRemoveTest {

    @Autowired
    private InterStateRepository interStateRepository;

    @Autowired
    private StatesHolder holder;

    static {
        // telegram context init
        ApiContextInitializer.init();
    }

    @Test
    public void addStateTest() throws Exception {
        // given
        Subscription sub = new Subscription(1, 200001, "Москва");
        ForecastData.WeatherType type = ForecastData.WeatherType.CLEAR;

        // add
        holder.addState(sub, type);

        // assert
        Assert.assertTrue(holder.getStates().containsKey(sub));
        Assert.assertTrue(holder.getStates().get(sub) == type);
    }

    @Test
    public void removeStateByUserIdAndCityState() throws Exception {
        // given
        long userId = 200001;
        String city = "Москва";
        Subscription sub = new Subscription(userId, 1, city);
        ForecastData.WeatherType type = ForecastData.WeatherType.CLEAR;

        // add
        holder.addState(sub, type);

        // assert
        Assert.assertEquals(1, holder.getStates().size());

        // remove
        holder.removeStateByUserIdAndCity(userId, city);

        // assert
        Assert.assertEquals(0, holder.getStates().size());
    }
}
