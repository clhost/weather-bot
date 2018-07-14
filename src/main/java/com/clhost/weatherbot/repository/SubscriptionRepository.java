package com.clhost.weatherbot.repository;

import com.clhost.weatherbot.entity.Subscription;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    void deleteByIdAndCity(final long id, @NotNull final String city);
}
