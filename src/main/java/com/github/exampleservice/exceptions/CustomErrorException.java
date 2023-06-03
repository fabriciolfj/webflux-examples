package com.github.exampleservice.exceptions;

import com.github.exampleservice.exceptions.dto.CustomErrorResponse;
import lombok.Getter;

public class CustomErrorException extends RuntimeException {
    @Getter
    private CustomErrorResponse errorResponse;

    public CustomErrorException(String message, CustomErrorResponse errorResponse) {
        super(message);
        this.errorResponse = errorResponse;
    }
}
