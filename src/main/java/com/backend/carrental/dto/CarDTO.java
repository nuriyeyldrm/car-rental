package com.backend.carrental.dto;

import com.backend.carrental.domain.FileDB;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    private Long id;

    private String model;

    private Integer doors;

    private Integer seats;

    private Integer luggage;

    private String transmission;

    private Boolean airConditioning;

    private Integer age;

    private Double pricePerHour;

    private String fuelType;

    private Object imageId;

    public CarDTO(Long id, String model, Integer doors, Integer seats, Integer luggage, String transmission,
                  Boolean airConditioning, Integer age, Double pricePerHour, String fuelType, Set<FileDB> imageId) {
        this.id = id;
        this.model = model;
        this.doors = doors;
        this.seats = seats;
        this.luggage = luggage;
        this.transmission = transmission;
        this.airConditioning = airConditioning;
        this.age = age;
        this.pricePerHour = pricePerHour;
        this.fuelType = fuelType;
        this.imageId = imageId.stream().map(image -> getImageId());
    }
}
