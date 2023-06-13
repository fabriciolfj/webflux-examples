package com.github.exampleservice.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("math.service")
public class MathVariablePathController {

    @MessageMapping("print.{input}")
    public Mono<Void> receive(@DestinationVariable int input) {
        System.out.println("value " + input);
        return Mono.empty();
    }
}
