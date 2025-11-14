package com.bilyoner.riskmanagement.config;

import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;

@Configuration
public class MicrometerConfig {

    @Bean
    public Counter betPlacedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("bet.placed.count")
                .description("Total number of bets placed")
                .register(meterRegistry);
    }
}
