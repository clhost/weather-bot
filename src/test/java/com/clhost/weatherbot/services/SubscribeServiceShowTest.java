package com.clhost.weatherbot.services;

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
@Sql("classpath:subscription_insert.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SubscribeServiceShowTest {

    @Autowired
    private SubscribeService subscribeService;

    static {
        // telegram context init
        ApiContextInitializer.init();
    }

    @Test
    public void testShowSubscriptions() throws Exception {
        // given
        long firstUserId = 200001;
        long secondUserId = 200002;

        // found
        long firstCount = subscribeService.showSubscriptions(firstUserId).size();
        long secondCount = subscribeService.showSubscriptions(secondUserId).size();

        // assert
        Assert.assertEquals(2, firstCount);
        Assert.assertEquals(1, secondCount);
    }
}
