package com.github.exampleservice.service;

import com.github.exampleservice.entities.Member;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberService {

    Flux<Member> findAll();

    Mono<Member> findById(final Long id);

    Mono<Member> save(final Member member);
}
