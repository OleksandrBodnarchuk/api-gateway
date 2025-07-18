package pl.alex.app.gateway.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "gateway.access")
public class GatewayAccessProperties {
    private List<String> openEndpoints;
    private TimeWindow timeWindow;

    @Data
    public static class TimeWindow {
        private boolean enabled;
        private ZonedDateTime start;
        private ZonedDateTime end;
    }
}