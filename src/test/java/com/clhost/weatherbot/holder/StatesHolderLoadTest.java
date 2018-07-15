package com.clhost.weatherbot.holder;

import com.clhost.weatherbot.repository.InterStateRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.ApiContextInitializer;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"classpath:subscription_insert.sql", "classpath:interstate_insert.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StatesHolderLoadTest {
    @Autowired
    private InterStateRepository interStateRepository;

    @Autowired
    private StatesHolder holder;

    static {
        // telegram context init
        ApiContextInitializer.init();
    }

    @Test
    public void loadStatesTest() throws Exception {
        // given
        int statesCount = 6;

        // load states
        holder.load();

        // assert
        System.out.println(holder.getStates().size());
        Assert.assertTrue(holder.getStates().size() == statesCount);
    }
}
