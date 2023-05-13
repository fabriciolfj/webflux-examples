package com.github.exampleservice.service;

import com.github.exampleservice.entities.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Slf4j
public class PersonService {

    public Flux<Person> listAllPerson() {
        return Flux.range(1, 10)
                .map(p -> Person.builder()
                        .code(UUID.randomUUID().toString())
                        .name("example :" + p)
                        .build());
    }

    public Mono<Void> savePerson(final Person person) {
        return Mono.just(person)
                .map(p -> {
                    log.info("person saved {}", person);
                    return p;
                }).log()
                .then();
    }
}
