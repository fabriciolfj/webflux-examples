package com.github.exampleservice;

import com.github.exampleservice.controller.dto.ComputationRequestDto;
import io.rsocket.metadata.WellKnownAuthType;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec13SecurityTest {

    private final MimeType TYPE = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());

    @Autowired
    private RSocketRequester.Builder builder;

    private RSocketRequester request;

    @BeforeAll
    public void setup() {
        UsernamePasswordMetadata metadata = new UsernamePasswordMetadata("client", "password");
        request = this.builder
                .setupMetadata(metadata, TYPE)
                .transport(TcpClientTransport.create("localhost", 6565));
    }

    @Test
    public void requestResponse() {
        UsernamePasswordMetadata metadata = new UsernamePasswordMetadata("user", "password");
        Mono<ComputationRequestDto> mono = request.route("math.service.secured.square")
                .metadata(metadata, TYPE)
                .data(new ComputationRequestDto(5))
                .retrieveMono(ComputationRequestDto.class)
                .doOnNext(System.out::println);

        StepVerifier
                .create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void requestResponseTable() {
        UsernamePasswordMetadata metadata = new UsernamePasswordMetadata("admin", "password");
        var flux = request.route("math.service.secured.table")
                .metadata(metadata, TYPE)
                .data(new ComputationRequestDto(5))
                .retrieveFlux(ComputationRequestDto.class)
                .doOnNext(System.out::println)
                .take(3);

        StepVerifier
                .create(flux)
                .expectNextCount(3)
                .verifyComplete();
    }
}
