package com.github.exampleservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Vehicle {

    @Id
    private String vehicleNo;
    private String color;
    private int wheel;
    private int seat;
}
