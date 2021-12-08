package com.backend.carrental.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 30)
    @NotNull(message = "Please enter car model")
    @Column(length = 30, nullable = false)
    private String model;

    private Integer doors;

    private Integer seats;

    private Integer luggage;

    @Column(length = 30)
    private String transmission;

    private Boolean airConditioning;

    private Integer age;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "car_image",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<FileDB> image;

    private Double pricePerHour;

    private String fuelType;

    public Car(String model, Integer doors, Integer seats, Integer luggage, String transmission,
               Boolean airConditioning, Integer age, Set<FileDB> image, Double pricePerHour, String fuelType) {
        this.model = model;
        this.doors = doors;
        this.seats = seats;
        this.luggage = luggage;
        this.transmission = transmission;
        this.airConditioning = airConditioning;
        this.age = age;
        this.image = image;
        this.pricePerHour = pricePerHour;
        this.fuelType = fuelType;
    }
}
