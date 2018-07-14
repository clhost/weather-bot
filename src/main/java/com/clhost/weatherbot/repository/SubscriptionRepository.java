package com.clhost.weatherbot.repository;

import com.clhost.weatherbot.entity.Subscription;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    void deleteByUserIdAndCity(final long userId, @NotNull final String city);
    List<Subscription> findAllByUserId(final long userId);
}
