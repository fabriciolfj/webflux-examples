package com.github.exampleservice.rsocket;

import com.github.exampleservice.client.CallbackService;
import com.github.exampleservice.rsocket.dto.RequestDTO;
import com.github.exampleservice.rsocket.util.ObjectUtil;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec02CallbackTest {

    private RSocket rSocket;

    @BeforeAll
    public void setup() {
        this.rSocket = RSocketConnector.create()
                .acceptor(SocketAcceptor.with(new CallbackService()))
                .connect(TcpClientTransport.create("localhost", 6565))
                .block();
    }

    @Test
    public void callback() throws InterruptedException {
        final var dto = new RequestDTO(5);
        Mono<Void> mono = rSocket.fireAndForget(ObjectUtil.toPayload(dto));

        StepVerifier.create(mono)
                .verifyComplete();

        System.out.println("going to wait");

        Thread.sleep(20000);
    }
}
