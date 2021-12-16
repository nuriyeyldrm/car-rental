package com.backend.carrental.dto;

import com.backend.carrental.domain.Car;
import com.backend.carrental.domain.FileDB;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
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

    private Set<String> image;

    private Boolean builtIn;

    public CarDTO(Car car) {
        this.id = car.getId();
        this.model = car.getModel();
        this.doors = car.getDoors();
        this.seats = car.getSeats();
        this.luggage = car.getLuggage();
        this.transmission = car.getTransmission();
        this.airConditioning = car.getAirConditioning();
        this.age = car.getAge();
        this.pricePerHour = car.getPricePerHour();
        this.fuelType = car.getFuelType();
        this.image = getImageId(car.getImage());
        this.builtIn = car.getBuiltIn();
    }

    public Set<String> getImageId(Set<FileDB> images) {
        Set<String> img = new HashSet<>();
        FileDB[] fileDB = images.toArray(new FileDB[images.size()]);

        for (int i = 0; i < images.size(); i++) {
            img.add(fileDB[i].getId());
        }
        return img;
    }
}
