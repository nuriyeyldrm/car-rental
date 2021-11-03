package com.backend.carrental.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car carId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    private Date pickUpTime;

    private Date dropOfTime;

    private String pickUpLocation;

    private String dropOfLocation;

    private Boolean status;

    public Reservation(Car carId, User userId, Date pickUpTime, Date dropOfTime, String pickUpLocation,
                       String dropOfLocation, Boolean status) {
        this.carId = carId;
        this.userId = userId;
        this.pickUpTime = pickUpTime;
        this.dropOfTime = dropOfTime;
        this.pickUpLocation = pickUpLocation;
        this.dropOfLocation = dropOfLocation;
        this.status = status;
    }
}
