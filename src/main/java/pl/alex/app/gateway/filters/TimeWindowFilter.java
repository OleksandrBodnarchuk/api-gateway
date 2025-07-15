package pl.alex.app.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeWindowFilter implements GlobalFilter {

    private final String gatewayAccessTimeWindowStart;
    private final String gatewayAccessTimeWindowEnd;

    public TimeWindowFilter(String start, String end) {
        this.gatewayAccessTimeWindowStart = start;
        this.gatewayAccessTimeWindowEnd = end;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ZonedDateTime start = ZonedDateTime.parse(gatewayAccessTimeWindowStart);
        final ZonedDateTime end = ZonedDateTime.parse(gatewayAccessTimeWindowEnd);

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        if (now.isBefore(start) || now.isAfter(end)) {
            exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
