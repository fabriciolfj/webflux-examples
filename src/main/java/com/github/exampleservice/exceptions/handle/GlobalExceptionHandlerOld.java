package com.github.exampleservice.exceptions.handle;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;


//@ControllerAdvice
public class GlobalExceptionHandlerOld {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseBody
    public Flux<ErrorMessage> handleValidationErrors(final WebExchangeBindException e) {
        return Flux.fromIterable(e.getFieldErrors())
                .map(this::toErrorMessage);
    }

    private ErrorMessage toErrorMessage(final FieldError fe) {
        return new ErrorMessage(fe.getField(), fe.getDefaultMessage());
    }
}
