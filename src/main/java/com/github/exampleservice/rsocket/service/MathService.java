package com.github.exampleservice.rsocket.service;

import com.github.exampleservice.rsocket.dto.RequestDTO;
import com.github.exampleservice.rsocket.dto.ResponseDTO;
import com.github.exampleservice.rsocket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
public class MathService implements RSocket {

    @Override
    public Mono<Void> fireAndForget(final Payload payload) {
        log.info("recebido: {}", ObjectUtil.toObject(payload, RequestDTO.class));
        return Mono.empty();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        return Mono.fromSupplier(() -> {
            final var request = ObjectUtil.toObject(payload, RequestDTO.class);
            final var response = new ResponseDTO(request.getInput(), request.getInput() * request.getInput());
            return ObjectUtil.toPayload(response);
        });
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        return Mono.just(payload)
                .map(p -> ObjectUtil.toObject(p, RequestDTO.class))
                .flux()
                .flatMap(p -> calculate(p.getInput()))
                .map(v -> ObjectUtil.toPayload(v));
    }

    private Flux<ResponseDTO> calculate(final int value) {
        return Flux.range(1, 10)
                .map(i -> new ResponseDTO(i, i * value))
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(System.out::println);
    }
}
