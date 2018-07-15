package com.clhost.weatherbot.repository;

import com.clhost.weatherbot.entity.InterState;
import com.clhost.weatherbot.holder.StatesHolderImpl;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!mock_interstate_repository")
public interface InterStateRepository  extends CrudRepository<InterState, Long> {}
