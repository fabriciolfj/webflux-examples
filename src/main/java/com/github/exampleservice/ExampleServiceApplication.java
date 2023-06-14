package com.github.exampleservice;

import com.github.exampleservice.entities.Vehicle;
import com.github.exampleservice.repository.VehicleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;

//@EnableWebFlux
@SpringBootApplication
@EnableR2dbcRepositories("com.github.exampleservice.repository")
public class ExampleServiceApplication implements CommandLineRunner {

    @Autowired
    private VehicleDao dao;

    public static void main(String[] args) {
        SpringApplication.run(ExampleServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var vehicle1 = new Vehicle("TEM0442", "Blue", 4, 4);
        var vehicle2 = new Vehicle("TEM0443", "Black", 4, 6);

        var latch = new CountDownLatch(1);

        var vehicles = Flux.just(vehicle1, vehicle2);

        vehicles.flatMap(dao::save)
                .thenMany(dao.findAll().log().doOnNext(System.out::println).flatMap(dao::delete))
                .doOnTerminate(latch::countDown).subscribe();

        latch.await();
    }
}
