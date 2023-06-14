package com.github.exampleservice;

import com.github.exampleservice.controller.dto.Response;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec04RSocketTest {

    private RSocketRequester rSocketRequester;

    @Autowired
    private RSocketRequester.Builder builder;

    @Autowired
    private RSocketMessageHandler handler;

    @BeforeAll
    public void setup() {
        this.rSocketRequester = this.builder
                .rsocketConnector(c -> c.acceptor(handler.responder()))
                .transport(TcpClientTransport.create("localhost", 6565));
    }

    @Test
    public void callbackTest() throws InterruptedException {
        Mono<Void> mono = this.rSocketRequester.route("batch.job.request").data(5).send();

        StepVerifier.create(mono)
                .verifyComplete();

        Thread.sleep(12000);

    }
}
