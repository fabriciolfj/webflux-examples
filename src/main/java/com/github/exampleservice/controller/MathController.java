package com.github.exampleservice.controller;

import com.github.exampleservice.controller.dto.ComputationRequestDto;
import com.github.exampleservice.controller.dto.ComputationResponseDto;
import com.github.exampleservice.rsocket.dto.ChartResponseDto;
import com.github.exampleservice.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class MathController {

    @Autowired
    private MathService mathService;

    @MessageMapping("math.service.print")
    public Mono<Void> print(Mono<ComputationRequestDto> responseDtoMono) {
        return this.mathService.print(responseDtoMono);
    }

    @MessageMapping("math.service.square")
    public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> requestDtoMono) {
        return this.mathService.findSquare(requestDtoMono);
    }

    @MessageMapping("math.service.table")
    public Flux<ComputationResponseDto> tableStream(Mono<ComputationRequestDto> requestDtoMono) {
        return requestDtoMono.flatMapMany(this.mathService::tableStream);
    }

    @MessageMapping("math.service.chart")
    public Flux<ChartResponseDto> chartStream(Flux<ComputationRequestDto> requestDtoFlux) {
        return this.mathService.chartStream(requestDtoFlux);
    }
}
