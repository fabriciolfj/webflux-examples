package com.github.exampleservice;

import com.github.exampleservice.controller.dto.ComputationRequestDto;
import com.github.exampleservice.controller.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.netty.tcp.TcpClient;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

@SpringBootTest
public class Lec12SslTest {

    static {
        System.setProperty("javax.net.ssl.trustStore", "/home/spark/Documentos/repo/webflux-examples/client.truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");
    }

    @Autowired
    private RSocketRequester.Builder builder;

    @Test
    public void sslTlsTest() throws InterruptedException {
        var req = this.builder
                .transport(TcpClientTransport.create(
                        TcpClient.create().host("localhost").port(6565).secure()
                ));

        var mono = req.route("math.service.square")
                .data(new ComputationRequestDto(5))
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println);

        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }
}
