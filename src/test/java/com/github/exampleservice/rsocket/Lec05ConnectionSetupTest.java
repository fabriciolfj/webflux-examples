package com.github.exampleservice.rsocket;

import com.github.exampleservice.rsocket.dto.RequestDTO;
import com.github.exampleservice.rsocket.dto.ResponseDTO;
import com.github.exampleservice.rsocket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec05ConnectionSetupTest {

    private RSocketClient rSocket;

    @BeforeAll
    public void setup() {
        Mono<RSocket> socketMono = RSocketConnector.create()
                .setupPayload(DefaultPayload.create("user1:password"))
                .connect(TcpClientTransport.create("localhost", 6565))
                .doOnNext(r -> System.out.println("going to connect"));

        this.rSocket = RSocketClient.from(socketMono);
    }

    @Test
    public void connectionTest() {

        Payload payload = ObjectUtil.toPayload(new RequestDTO(5));

        Flux<ResponseDTO> flux1 = rSocket.requestStream(Mono.just(payload))
                .map(p -> ObjectUtil.toObject(p, ResponseDTO.class))
                .doOnNext(System.out::println);

        StepVerifier.create(flux1)
                .expectNextCount(3)
                .verifyComplete();
    }
}
