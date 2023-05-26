package com.github.exampleservice.rsocket.service;

import com.github.exampleservice.rsocket.dto.RequestDTO;
import com.github.exampleservice.rsocket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
@Slf4j
public class MathService implements RSocket {

    @Override
    public Mono<Void> fireAndForget(final Payload payload) {
        log.info("receiving: {}", ObjectUtil.toObject(payload, RequestDTO.class));
        return Mono.empty();
    }
}
