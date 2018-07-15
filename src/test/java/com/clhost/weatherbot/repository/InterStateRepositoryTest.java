package com.clhost.weatherbot.repository;

import com.clhost.weatherbot.entity.ForecastData;
import com.clhost.weatherbot.entity.InterState;
import com.clhost.weatherbot.entity.Subscription;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@Sql("classpath:subscription_insert.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DataJpaTest
public class InterStateRepositoryTest {

    @Autowired
    private InterStateRepository repository;

    @Test
    public void testFindOne() throws Exception {
        // given
        long rowId = 1;
        Subscription sub = new Subscription(200001, 1, "Москва");
        InterState state = new InterState(sub, ForecastData.WeatherType.CLEAR);

        // save
        repository.save(state);

        // found
        Optional<InterState> found = repository.findById(rowId);

        // assert
        Assert.assertEquals(state, found.orElse(null));
    }

    @Test
    public void testFindAll() throws Exception {
        // given
        List<Subscription> subs = new ArrayList<>();
        subs.add(new Subscription(1, 200001, 1, "Москва"));
        subs.add(new Subscription(2, 200002, 2, "New-York"));
        subs.add(new Subscription(3, 200003, 3, "London"));
        subs.add(new Subscription(4, 200004, 4, "Санкт-Петербург"));
        subs.add(new Subscription(5, 200005, 5, "Иркутск"));

        List<InterState> states = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            states.add(new InterState(subs.get(i), ForecastData.WeatherType.values()[i]));
        }

        // save
        repository.saveAll(states);

        // found
        List<InterState> found = (List<InterState>) repository.findAll();

        // assert
        Assert.assertEquals(states, found);
    }
}
