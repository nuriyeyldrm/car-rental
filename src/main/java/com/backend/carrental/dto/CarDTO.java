package com.backend.carrental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    private String model;

    private Integer doors;

    private Integer seats;

    private Integer luggage;

    private Integer transmission;

    private Boolean airConditioning;

    private Integer age;

    private Double pricePerDay;

    private String fuelType;

    private String id;

    private String name;

    private String type;
}
