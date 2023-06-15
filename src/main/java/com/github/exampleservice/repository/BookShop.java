package com.github.exampleservice.repository;

import reactor.core.publisher.Mono;

public interface BookShop {

    Mono<Void> purchase(String isbn, String username);
}
