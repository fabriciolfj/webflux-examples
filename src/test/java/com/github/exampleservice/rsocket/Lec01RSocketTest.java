package com.github.exampleservice.rsocket;

import com.github.exampleservice.rsocket.dto.ChartResponseDto;
import com.github.exampleservice.rsocket.dto.RequestDTO;
import com.github.exampleservice.rsocket.dto.ResponseDTO;
import com.github.exampleservice.rsocket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec01RSocketTest {

    private RSocket rSocket;

    @BeforeAll
    public void setup() {
        this.rSocket = RSocketConnector.create()
                .connect(TcpClientTransport.create("localhost", 6565))
                .block();
    }

    @Test
    public void fireAndForget() {
        final Payload payload = ObjectUtil.toPayload(new RequestDTO(100));
        final Mono<Void> mono = this.rSocket.fireAndForget(payload);

        StepVerifier.create(mono)
               // .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void requestResponse() {
        final Payload payload = ObjectUtil.toPayload(new RequestDTO(5));
        final Mono<ResponseDTO> mono = this.rSocket.requestResponse(payload)
                .log()
                .map(p -> ObjectUtil.toObject(p, ResponseDTO.class))
                .doOnNext(System.out::println);

        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void requestStream() {
        Flux<Payload> payloadFlux = Flux.range(-10, 21)
                .delayElements(Duration.ofMillis(500))
                .map(RequestDTO::new)
                .map(ObjectUtil::toPayload);

        Flux<ChartResponseDto> chartResponseDtoFlux = this.rSocket.requestChannel(payloadFlux)
                .map(p -> ObjectUtil.toObject(p, ChartResponseDto.class))
                .doOnNext(System.out::println);

        StepVerifier.create(chartResponseDtoFlux)
                .expectNextCount(21)
                .verifyComplete();
    }
}

