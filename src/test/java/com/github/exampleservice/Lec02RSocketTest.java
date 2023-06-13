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
public class Lec02RSocketTest {

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
        final Mono<Void> send = this.rSocketRequester.route("math.service.print.55")
                .send();

        StepVerifier.create(send)
                // .expectNextCount(1)
                .verifyComplete();
    }
}
