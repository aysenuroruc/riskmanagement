package com.bilyoner.riskmanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@ConfigurationProperties(prefix = "betting")
@Data
public class BettingConfigProperties {
    private OddsConfig odds = new OddsConfig();
    private RiskConfig risk = new RiskConfig();

    @Data
    public static class OddsConfig {
        private BigDecimal minValue = BigDecimal.valueOf(1.00);
        private BigDecimal reductionCoefficient = BigDecimal.valueOf(0.15);
        private BigDecimal increaseCoefficient = BigDecimal.valueOf(0.05);
    }

    @Data
    public static class RiskConfig {
        private BigDecimal houseEdgeMargin = BigDecimal.valueOf(0.05);
    }
}
