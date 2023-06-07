package com.github.exampleservice.service;

import com.github.exampleservice.controller.dto.ComputationRequestDto;
import com.github.exampleservice.controller.dto.ComputationResponseDto;
import com.github.exampleservice.rsocket.dto.ChartResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MathService {

    public Mono<Void> print(Mono<ComputationRequestDto> dto) {
        return dto
                .doOnNext(System.out::println)
                .then();
    }

    public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> dto) {
        return dto.map(ComputationRequestDto::getInput)
                .map(c -> new ComputationResponseDto(c, c * c));
    }

    public Flux<ComputationResponseDto> tableStream(ComputationRequestDto dto) {
        return Flux.range(1, 10)
                .map(i -> new ComputationResponseDto(dto.getInput(), dto.getInput() * i));
    }

    public Flux<ChartResponseDto> chartStream(Flux<ComputationRequestDto> requestDtoFlux) {
        return requestDtoFlux
                .map(ComputationRequestDto::getInput)
                .map(i -> new ChartResponseDto(i, (i * i) + 1));
    }
}
