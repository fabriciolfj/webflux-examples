package com.github.exampleservice.rsocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientConnectionRequest {

    private String clientId;
    private String secretKey;
}
