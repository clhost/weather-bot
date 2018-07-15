package com.clhost.weatherbot.services;

import com.clhost.weatherbot.entity.Subscription;
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
public class SubscribeServiceSubUnsubTest {

    @Autowired
    private SubscribeService subscribeService;

    static {
        // telegram context init
        ApiContextInitializer.init();
    }

    @Test
    public void testSubscribe() throws Exception {
        // given
        long userId = 200001;
        String city = "Санкт-Петербург";
        Subscription sub = new Subscription(userId, 1, city);

        // subscribe
        subscribeService.subscribe(sub);

        // found
        List<Subscription> subs = subscribeService.showSubscriptions(userId);

        // assert
        Assert.assertTrue(subs.size() == 1 && subs.get(0).equals(sub));
    }

    @Test
    public void testUnsubscribe() throws Exception {
        // given
        long userId = 200001;
        String city = "Санкт-Петербург";
        Subscription sub = new Subscription(userId, 1, city);

        // subscribe
        subscribeService.subscribe(sub);

        // unsubscribe
        subscribeService.unsubscribe(userId, city);

        // found
        List<Subscription> subs = subscribeService.showSubscriptions(userId);

        // assert
        Assert.assertTrue(subs.isEmpty());

    }
}
