package com.github.exampleservice.controller;

import com.github.exampleservice.entities.Person;
import com.github.exampleservice.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons/api/v1")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public Flux<Person> getPersons() {
        return personService.listAllPerson();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> savePerson(@RequestBody final Mono<Person> person) {
        return person.flatMap(personService::savePerson);
    }
}
