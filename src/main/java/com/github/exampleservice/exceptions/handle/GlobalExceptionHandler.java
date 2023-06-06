package com.github.exampleservice.exceptions.handle;

import com.github.exampleservice.exceptions.CustomErrorException;
import com.github.exampleservice.exceptions.UserNotFoundException;
import com.github.exampleservice.exceptions.dto.CustomErrorResponse;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomErrorException.class)
    protected ResponseEntity<CustomErrorResponse> handleCustomError(CustomErrorException ex) {
        return ResponseEntity.status(ex.getErrorResponse().getStatus()).body(ex.getErrorResponse());
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ProblemDetail handleNotFound(RuntimeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("User not found");
        problemDetail.setType(URI.create("https://example.com/problems/user-not-found"));
        problemDetail.setProperty("errors", List.of(ErrorDetails.API_USER_NOT_FOUND));
        return problemDetail;
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          ServerWebExchange exchange) {
        var locale = exchange.getLocaleContext().getLocale();
        assert locale != null;
        var errors = ex.resolveErrorMessages(Objects.requireNonNull(getMessageSource()), locale);
        ex.getBody().setProperty("errors", errors.values());
        return super.handleExceptionInternal(ex, null, headers, status, exchange);
    }
}
