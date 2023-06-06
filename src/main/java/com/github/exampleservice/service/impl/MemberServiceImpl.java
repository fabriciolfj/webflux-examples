package com.github.exampleservice.service.impl;

import com.github.exampleservice.entities.Member;
import com.github.exampleservice.service.MemberService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MemberServiceImpl implements MemberService {

    private final AtomicLong sequence = new AtomicLong(1);
    private final Map<Long, Member> concurrent = new ConcurrentHashMap<>();

    @Override
    public Flux<Member> findAll() {
        return Flux.fromIterable(concurrent.values());
    }

    @Override
    public Mono<Member> findById(Long id) {
        return Mono.justOrEmpty(concurrent.get(id));
    }

    @Override
    public Mono<Member> save(Member member) {

        return Mono.just(member)
                .flatMap(p -> {
                    var id = sequence.incrementAndGet();
                    concurrent.put(id, p);
                    return Mono.just(p);
                });
    }

    @PostConstruct
    private void init() {
        Flux.just(
                Member.builder().name("teste")
                        .email("teste@gmail.com").build(),
                Member.builder().name("teste2")
                        .email("teste2@gmail.com").build()
        ).flatMap(this::save)
                .subscribe();
    }
}
