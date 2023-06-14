package com.github.exampleservice.repository.impl;

import com.github.exampleservice.entities.Vehicle;
import com.github.exampleservice.repository.VehicleDao;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;

@Repository
public class R2dbcDao implements VehicleDao {

    private final R2dbcEntityTemplate template;

    public R2dbcDao(R2dbcEntityTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Vehicle> save(Vehicle vehicle) {
        return template.insert(vehicle);
    }

    @Override
    public Mono<Vehicle> findByVehicleNo(String vehicleNo) {
        var query = Query.query(where("vehicleNo").is(vehicleNo));
        return template.selectOne(query, Vehicle.class);
    }

    @Override
    public Flux<Vehicle> findAll() {
        return template.select(Vehicle.class).all();
    }

    @Override
    public Mono<Void> delete(Vehicle vehicle) {
        return template.delete(vehicle).then();
    }
}
