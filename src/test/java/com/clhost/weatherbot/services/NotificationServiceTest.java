package com.clhost.weatherbot.services;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.ApiContextInitializer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class NotificationServiceTest {
    @Rule
    public final Timeout timeout = Timeout.seconds(5);

    @Autowired
    private NotificationService notificationService;

    static {
        // telegram context init
        ApiContextInitializer.init();
    }

    @Test
    public void testInterrupt() throws Exception {
        // given
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // start
        Future future = executorService.submit(() -> notificationService.loop());

        // wait
        TimeUnit.SECONDS.sleep(2);

        // interrupt
        notificationService.interrupt();

        // assert
        // noinspection StatementWithEmptyBody
        while (!future.isDone()) {}
        Assert.assertTrue(future.isDone());
    }
}
