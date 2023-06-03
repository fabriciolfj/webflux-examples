package com.github.exampleservice.exceptions.handle;

import com.github.exampleservice.exceptions.CustomErrorException;
import com.github.exampleservice.exceptions.UserNotFoundException;
import com.github.exampleservice.exceptions.dto.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
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
}
