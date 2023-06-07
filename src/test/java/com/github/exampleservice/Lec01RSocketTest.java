package com.github.exampleservice;

import com.github.exampleservice.controller.dto.ComputationRequestDto;
import com.github.exampleservice.controller.dto.ComputationResponseDto;
import com.github.exampleservice.rsocket.dto.ChartResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec01RSocketTest {

    private RSocketRequester rSocketRequester;

    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void setup() {
        this.rSocketRequester = this.builder
                .transport(TcpClientTransport.create("localhost", 6565));
    }

    @Test
    public void fireAndForget() {
        final Mono<Void> send = this.rSocketRequester.route("math.service.print").data(new ComputationRequestDto(5))
                .send();

        StepVerifier.create(send)
               // .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void findSquare() {
        final Mono<ComputationResponseDto> send = this.rSocketRequester.route("math.service.square")
                .data(new ComputationRequestDto(5))
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(send)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void requestStream() {
        final Flux<ComputationResponseDto> send = this.rSocketRequester.route("math.service.table")
                .data(new ComputationRequestDto(5))
                .retrieveFlux(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(send)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void chartStream() {
        var data = Flux.range(-10, 21).map(ComputationRequestDto::new);
        final Flux<ChartResponseDto> send = this.rSocketRequester.route("math.service.chart")
                .data(data)
                .retrieveFlux(ChartResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(send)
                .expectNextCount(21)
                .verifyComplete();
    }


}
