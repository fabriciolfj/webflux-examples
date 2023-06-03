package com.github.exampleservice.controller;

import com.github.exampleservice.controller.dto.UserResponseDTO;
import com.github.exampleservice.exceptions.CustomErrorException;
import com.github.exampleservice.exceptions.UserNotFoundException;
import com.github.exampleservice.exceptions.dto.CustomErrorResponse;
import com.github.exampleservice.exceptions.handle.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    final static Map<Long, UserResponseDTO> USERS = Map.of(
            1L, new UserResponseDTO(1L, "Fabricio"),
            2L, new UserResponseDTO(2L, "Jacob"));

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<UserResponseDTO>> getUserById(@PathVariable Long userId) {
        return Mono.fromCallable(() -> {
            final UserResponseDTO dto = USERS.get(userId);

            if (dto == null) {
                CustomErrorResponse customErrorResponse = CustomErrorResponse
                        .builder()
                        .traceId(UUID.randomUUID().toString())
                        .timestamp(OffsetDateTime.now().now())
                        .status(HttpStatus.NOT_FOUND)
                        .errors(List.of(ErrorDetails.API_USER_NOT_FOUND))
                        .build();
                throw new CustomErrorException("User not found", customErrorResponse);
                //throw new UserNotFoundException("user not found");
            }

            return new ResponseEntity<>(dto, HttpStatus.OK);
        });
    }
}
