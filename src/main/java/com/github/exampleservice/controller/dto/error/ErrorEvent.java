package com.github.exampleservice.controller.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorEvent {

    private StatusCode statusCode;
    private final LocalDate date = LocalDate.now();
}
