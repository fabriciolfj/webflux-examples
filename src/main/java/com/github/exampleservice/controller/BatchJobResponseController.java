package com.github.exampleservice.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class BatchJobResponseController {

    @MessageMapping("batch.job.response")
    public Mono<Void> response(Mono<Integer> integerMono) {
        return integerMono
                .doOnNext(c -> System.out.println("value received " + c))
                .then();
    }
}
