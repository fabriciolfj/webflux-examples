package com.github.exampleservice.controller;

import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class ConnectionHandler {

    @ConnectMapping
    public Mono<Void> handleConnection() {
        System.out.println("connection setup");
        return Mono.empty();
    }
}
