package pl.alex.app.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import pl.alex.app.gateway.configuration.GatewayAccessProperties;
import pl.alex.app.gateway.util.JwtUtil;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtTokenFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final List<String> openEndpoints;
    private final GatewayAccessProperties.TimeWindow timeWindow;

    public JwtTokenFilter(JwtUtil jwtUtil, GatewayAccessProperties properties) {
        this.jwtUtil = jwtUtil;
        this.openEndpoints = properties.getOpenEndpoints();
        this.timeWindow = properties.getTimeWindow();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (openEndpoints.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE; // високий пріоритет
    }
}
