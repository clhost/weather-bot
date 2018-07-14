package com.clhost.weatherbot.repository;

import com.clhost.weatherbot.holder.StatesHolderImpl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterStateRepository  extends CrudRepository<StatesHolderImpl.InterState, Long> {}
