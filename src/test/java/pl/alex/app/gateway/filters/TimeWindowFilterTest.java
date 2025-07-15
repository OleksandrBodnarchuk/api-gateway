package pl.alex.app.gateway.filters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.*;

class TimeWindowFilterTest {

    @Test
    @DisplayName("Test dates out of range should return SERVICE_UNAVAILABLE response status")
    void timeOutOfRange_shouldReturn_serviceUnavailableStatus() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime start = now.minusMinutes(5);
        ZonedDateTime end = now.minusMinutes(1);
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test").build());
        TimeWindowFilter filter = new TimeWindowFilter(start.toString(), end.toString());

        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        assert exchange.getResponse().getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE;
        verify(chain, never()).filter(exchange);
    }

    @Test
    @DisplayName("Time within window should allow request and call filter chain")
    void timeInRange_shouldCallFilterMethod() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime start = now.minusMinutes(5);
        ZonedDateTime end = now.plusMinutes(5);

        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test").build()
        );

        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        TimeWindowFilter filter = new TimeWindowFilter(start.toString(), end.toString());
        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(chain).filter(exchange);
        assert exchange.getResponse().getStatusCode() == null;
    }

}