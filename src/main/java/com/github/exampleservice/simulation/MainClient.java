package com.github.exampleservice.simulation;

import com.github.exampleservice.entities.Member;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) throws IOException {
        var url = "http://localhost:8080";
        WebClient.create(url)
                .get()
                .uri("/api/v1/members")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToFlux(c -> c.bodyToFlux(Member.class))
                .subscribe(System.out::println);

        System.in.read();
    }
}
