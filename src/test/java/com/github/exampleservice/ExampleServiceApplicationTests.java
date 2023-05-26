package com.github.exampleservice;

import com.github.exampleservice.controller.PersonController;
import com.github.exampleservice.entities.Person;
import com.github.exampleservice.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {PersonController.class})
class ExampleServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private PersonService personService;

	@Test
	public void givenPerson_created() {
		final Person person = Person.builder()
				.name("teste")
				.code(UUID.randomUUID().toString())
				.build();

		given(personService.savePerson(any())).willReturn(Mono.when());

		final WebTestClient.ResponseSpec response = webTestClient.post()
				.uri("/persons/api/v1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(person), Person.class)
				.exchange();

		response.expectStatus()
				.isCreated();
	}

	@Test
	public void givenPerson_findAll() {
		final List<Person> persons = Arrays.asList(
				Person.builder().code(UUID.randomUUID().toString())
						.name("teste one").build(),
				Person.builder().code(UUID.randomUUID().toString())
						.name("teste two").build()
		);

		given(personService.listAllPerson()).willReturn(Flux.fromIterable(persons));

		final WebTestClient.ResponseSpec response = webTestClient.get().uri("/persons/api/v1")
				.accept(MediaType.APPLICATION_JSON)
				.exchange();

		response.expectStatus().isOk()
				.expectBody()
				.consumeWith(System.out::println)
				.jsonPath("$[0].name").isEqualTo("teste one")
				.jsonPath("$[1].name").isEqualTo("teste two");
	}

}
