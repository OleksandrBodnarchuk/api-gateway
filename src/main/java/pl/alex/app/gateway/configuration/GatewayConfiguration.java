package pl.alex.app.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.alex.app.gateway.filters.TimeWindowFilter;

@Configuration
public class GatewayConfiguration {

    @Bean
    @ConditionalOnProperty(name = "gateway.access.time-window.enabled", havingValue = "true")
    public TimeWindowFilter timeWindowFilter(@Value("${gateway.access.time-window.start}") String start,
                                             @Value("${gateway.access.time-window.end}") String end) {
        return new TimeWindowFilter(start, end);
    }
}
