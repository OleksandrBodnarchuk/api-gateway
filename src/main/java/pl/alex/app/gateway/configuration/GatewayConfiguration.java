package pl.alex.app.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.alex.app.gateway.filters.JwtTokenFilter;
import pl.alex.app.gateway.filters.TimeWindowFilter;
import pl.alex.app.gateway.util.JwtUtil;

@Configuration
public class GatewayConfiguration {

    @Bean
    @ConditionalOnProperty(name = "gateway.access.time-window.enabled", havingValue = "true")
    public TimeWindowFilter timeWindowFilter(@Value("${gateway.access.time-window.start}") String start,
                                             @Value("${gateway.access.time-window.end}") String end) {
        return new TimeWindowFilter(start, end);
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(JwtUtil jwtUtil, GatewayAccessProperties gatewayAccessProperties) {
        return new JwtTokenFilter(jwtUtil, gatewayAccessProperties);
    }
}
