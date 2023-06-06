package com.github.exampleservice.controller;

import com.github.exampleservice.entities.Member;
import com.github.exampleservice.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public Flux<Member> getAll() {
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Member> findById(@PathVariable("id") final Long id) {
        return memberService.findById(id);
    }

    @PostMapping
    public Mono<Member> create(@Valid @RequestBody final Member member) {
        return memberService.save(member);
    }
}
