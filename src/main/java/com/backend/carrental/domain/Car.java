package com.backend.carrental.domain;

import com.backend.carrental.domain.enumeration.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
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
    @NotNull(message = "Please enter the car model")
    @Column(length = 30, nullable = false)
    private String model;

    @NotNull(message = "Please enter the car doors")
    @Column(nullable = false)
    private Integer doors;

    @NotNull(message = "Please enter the car seats")
    @Column(nullable = false)
    private Integer seats;

    @NotNull(message = "Please enter the car luggage")
    @Column(nullable = false)
    private Integer luggage;

    @NotNull(message = "Please enter the car transmission")
    @Column(length = 30, nullable = false)
    private String transmission;

    @NotNull(message = "Please enter the car air conditioning")
    @Column(nullable = false)
    private Boolean airConditioning;

    @NotNull(message = "Please enter the car age")
    @Column(nullable = false)
    private Integer age;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "car_image",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id"))
    private Set<FileDB> image;

    @NotNull(message = "Please enter price per hour of the car")
    @Column(nullable = false)
    private Double pricePerHour;

    @NotNull(message = "Please enter the car fuel type")
    @Column(nullable = false)
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
