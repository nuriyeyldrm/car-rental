package com.backend.carrental.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 15)
    @NotNull(message = "Please enter the car model")
    @Column(nullable = false, length = 15)
    private String model;

    @NotNull(message = "Please enter the number of doors")
    @Column(nullable = false)
    private Integer doors;

    @NotNull(message = "Please enter the number of seats")
    @Column(nullable = false)
    private Integer seats;

    @NotNull(message = "Please enter the number of luggage")
    @Column(nullable = false)
    private Integer luggage;
}
