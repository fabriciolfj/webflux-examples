package com.github.exampleservice.rsocket.service;

import com.github.exampleservice.rsocket.dto.RequestDTO;
import com.github.exampleservice.rsocket.dto.ResponseDTO;
import com.github.exampleservice.rsocket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class BatchJobService implements RSocket {

    private RSocket rSocket;

    public BatchJobService(final RSocket rSocket) {
        this.rSocket = rSocket;
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        RequestDTO requestDTO = ObjectUtil.toObject(payload, RequestDTO.class);
        System.out.println("received: " + requestDTO);

        Mono.just(requestDTO)
                .delayElement(Duration.ofSeconds(5))
                .doOnNext(i -> System.out.println("emitting"))
                .flatMap(this::findCube)
                .subscribe();

        return Mono.empty();
    }

    private Mono<Void> findCube(final RequestDTO requestDTO) {
        int input = requestDTO.getInput();
        int output = input * input * input;
        final var response = new ResponseDTO(input, output);
        final Payload payload = ObjectUtil.toPayload(response);

        return this.rSocket.fireAndForget(payload);
    }
}
