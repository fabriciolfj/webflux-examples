package com.github.exampleservice.controller;

import com.github.exampleservice.controller.dto.Response;
import com.github.exampleservice.controller.dto.error.ErrorEvent;
import com.github.exampleservice.controller.dto.error.StatusCode;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("math.validation")
public class ValidationController {

    @MessageMapping("double.{input}")
    public Mono<Integer> validation(@DestinationVariable int input) {
        return Mono.just(input)
                .filter(i -> i <= 30)
                .map(i -> i * 2)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new IllegalArgumentException("cannot > 30"))));
    }

    @MessageMapping("double.response.{input}")
    public Mono<Response<Integer>> doubleResponse(@DestinationVariable int input) {
        return Mono.just(input)
                .filter(i -> i <= 30)
                .map(i -> i * 2)
                .map(Response::with)
                .defaultIfEmpty(Response.with(new ErrorEvent(StatusCode.EC001)));
    }

    @MessageExceptionHandler(IllegalArgumentException.class)
    public Mono<Integer> handleException(Exception e) {
        return Mono.just(-1);
    }
}
