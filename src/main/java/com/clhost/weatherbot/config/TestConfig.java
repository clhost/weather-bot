package com.clhost.weatherbot.config;

import com.clhost.weatherbot.repository.InterStateRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class TestConfig {
    @Bean
    @Primary
    @Profile("mock_interstate_repository")
    public InterStateRepository interStateRepository() {
        return Mockito.mock(InterStateRepository.class);
    }
}
