package com.github.exampleservice.repository;

import com.github.exampleservice.entities.Vehicle;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VehicleDao {

    Mono<Vehicle> save(Vehicle vehicle);
    Mono<Vehicle> findByVehicleNo(String vehicleNo);
    Flux<Vehicle> findAll();
    Mono<Void> delete(Vehicle vehicle);
}
