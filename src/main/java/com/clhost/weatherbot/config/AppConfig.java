package com.clhost.weatherbot.config;

import com.clhost.weatherbot.logger.LoggingAnnotationProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public LoggingAnnotationProcessor loggingAnnotationProcessor() {
        return new LoggingAnnotationProcessor();
    }
}
