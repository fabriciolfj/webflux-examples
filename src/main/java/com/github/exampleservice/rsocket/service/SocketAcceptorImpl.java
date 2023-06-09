package com.github.exampleservice.rsocket.service;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class SocketAcceptorImpl implements SocketAcceptor {

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
        log.info("socket acceptor impl-accept method");
        //return Mono.just(new MathService());
       // return Mono.fromCallable(() -> new BatchJobService(sendingSocket));

        if (isValidClient(setup.getDataUtf8())) {
            return Mono.just(new MathService());
        } else {
            return Mono.just(new FreeServivce());
        }

        //return Mono.just(new FastProducerService());
    }

    private boolean isValidClient(final String credentials) {
        return "user:password".equals(credentials);
    }
}
