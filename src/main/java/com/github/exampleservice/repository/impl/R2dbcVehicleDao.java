package com.github.exampleservice.repository.impl;

import com.github.exampleservice.entities.Vehicle;
import com.github.exampleservice.repository.VehicleDao;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.Statement;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*@Repository
public class R2dbcVehicleDao implements VehicleDao {

    private static final String INSERT_SQL = "INSERT INTO VEHICLE (COLOR, WHEEL, SEAT, VEHICLE_NO) VALUES ($1, $2, $3, $4)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM VEHICLE";
    private static final String SELECT_ONE_SQL = "SELECT * FROM VEHICLE WHERE VEHICLE_NO = $1";
    private static final String DELETE_SQL = "DELETE FROM VEHICLE WHERE VEHICLE_NO=$1";
    private final DatabaseClient databaseClient;

    public R2dbcVehicleDao(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Vehicle> save(Vehicle vehicle) {
        return prepareStatement(databaseClient.sql(INSERT_SQL), vehicle)
                .fetch()
                .rowsUpdated().doOnNext(c -> System.out.printf("Rows inserted: %d%n", c))
                .then(this.findByVehicleNo(vehicle.getVehicleNo()));
    }

    @Override
    public Mono<Vehicle> findByVehicleNo(String vehicleNo) {
        return databaseClient.sql(SELECT_ONE_SQL)
                .bind("$1", vehicleNo)
                .map((r, rmd) -> toVehicle(r))
                .one();
    }

    @Override
    public Flux<Vehicle> findAll() {
        return databaseClient.sql(SELECT_ALL_SQL)
                .map((r, rmd) -> toVehicle(r))
                .all();
    }

    @Override
    public Mono<Void> delete(Vehicle vehicle) {
        return databaseClient.sql(DELETE_SQL)
                .bind("$1", vehicle.getVehicleNo())
                .fetch()
                .rowsUpdated()
                .doOnNext(c -> System.out.println("rows deleted " + c))
                .then();
    }

    private DatabaseClient.GenericExecuteSpec prepareStatement(DatabaseClient.GenericExecuteSpec st, Vehicle vehicle) {
        return st.bind("$1", vehicle.getColor())
                .bind("$2", vehicle.getWheel())
                .bind("$3", vehicle.getSeat())
                .bind("$4", vehicle.getVehicleNo());
    }

    private Vehicle toVehicle(Row row) {
        return new Vehicle(row.get("VEHICLE_NO", String.class),
                row.get("COLOR", String.class),
                row.get("WHEEL", Integer.class),
                row.get("SEAT", Integer.class));
    }
}*/
