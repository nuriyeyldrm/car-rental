package com.backend.carrental.domain;

import com.backend.carrental.domain.enumeration.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.*;


import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    private Car carId;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

//    @Temporal(TemporalType.TIMESTAMP)
//    @FutureOrPresent(message = "Please enter valid date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy HH:mm:ss", timezone = "Turkey")
    private LocalDateTime pickUpTime;

//    @Temporal(TemporalType.TIMESTAMP)
//    @FutureOrPresent(message = "Please enter valid date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy HH:mm:ss", timezone = "Turkey")
    private LocalDateTime dropOfTime;

    @Column(length = 50)
    private String pickUpLocation;

    @Column(length = 50)
    private String dropOfLocation;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private ReservationStatus status;

    private Double totalPrice;

    public Reservation(LocalDateTime pickUpTime, LocalDateTime dropOfTime, String pickUpLocation, String dropOfLocation,
                       ReservationStatus status) {
        this.pickUpTime = pickUpTime;
        this.dropOfTime = dropOfTime;
        this.pickUpLocation = pickUpLocation;
        this.dropOfLocation = dropOfLocation;
        this.status = status;
    }

    public long getTotalHours(LocalDateTime pickUpTime, LocalDateTime dropOfTime) {

        long seconds = ChronoUnit.SECONDS.between(pickUpTime, dropOfTime);
        long minutes = ChronoUnit.MINUTES.between(pickUpTime, dropOfTime);
        long hours = ChronoUnit.HOURS.between(pickUpTime, dropOfTime);
        long days = ChronoUnit.DAYS.between(pickUpTime, dropOfTime);


        return hours;
    }
}
