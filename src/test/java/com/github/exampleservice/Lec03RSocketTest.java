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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec03RSocketTest {

    private RSocketRequester rSocketRequester;

    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void setup() {
        this.rSocketRequester = this.builder
                .transport(TcpClientTransport.create("localhost", 6565));
    }

    @Test
    public void request() {
        final Mono<Integer> send = this.rSocketRequester.route("math.validation.double.31")
                .retrieveMono(Integer.class)
                .onErrorReturn(Integer.MIN_VALUE)
                .doOnNext(System.out::println);

        StepVerifier.create(send)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void requestResponse() {
        Mono<Response<Integer>> mono = this.rSocketRequester.route("math.validation.double.response.30")
                .retrieveMono(new ParameterizedTypeReference<Response<Integer>>() {
                })
                .doOnNext(r -> {
                    if(r.hasError()) {
                        System.out.println(r.getErrorResponse().getStatusCode().getDescription());
                    } else {
                        System.out.println(r.getSuccessResponse());
                    }
                });

        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }
}
