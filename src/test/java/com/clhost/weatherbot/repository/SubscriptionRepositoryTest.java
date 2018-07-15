package com.clhost.weatherbot.repository;

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
public class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository repository;

    @Test
    public void testFindOne() throws Exception {
        // given
        long rowId = 1;
        Subscription sub = new Subscription(200001, 1, "Москва");

        //found
        Optional<Subscription> found = repository.findById(rowId);

        // assert
        Assert.assertEquals(sub, found.orElse(null));
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
        subs.add(new Subscription(6, 200001, 3, "Иркутск"));

        // found
        List<Subscription> found = (List<Subscription>) repository.findAll();

        // assert
        Assert.assertEquals(subs, found);
    }

    @Test
    public void testDeleteByUserIdAndCity() throws Exception {
        // given
        long userId = 200001;
        long rowId = 1;
        String city = "Москва";

        // is exists
        Assert.assertTrue(repository.findById(rowId).isPresent());

        // delete
        repository.deleteByUserIdAndCity(userId, city);

        // assert
        Assert.assertFalse(repository.findById(userId).isPresent());
    }

    @Test
    public void findAllByUserId() throws Exception {
        // given
        long userId = 200001;
        List<Subscription> subs = new ArrayList<>();
        subs.add(new Subscription(1, userId, 1, "Москва"));
        subs.add(new Subscription(6, userId, 3, "Иркутск"));
        subs.add(new Subscription(2, 200002, 2, "New-York"));
        subs.add(new Subscription(3, 200003, 3, "London"));
        subs.add(new Subscription(4, 200004, 4, "Санкт-Петербург"));
        subs.add(new Subscription(5, 200005, 5, "Иркутск"));

        // found
        List<Subscription> found = repository.findAllByUserId(userId);

        // assert
        Assert.assertEquals(subs.subList(0, 2), found);
    }
}















