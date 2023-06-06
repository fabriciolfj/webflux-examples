package com.github.exampleservice.rsocket.service;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

public class FastProducerService implements RSocket {

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return Flux.range(1, 1000)
                .map(i -> i + " ")
                .doOnNext(System.out::println)
                .doFinally(System.out::println)
                .map(DefaultPayload::create);
    }
}
