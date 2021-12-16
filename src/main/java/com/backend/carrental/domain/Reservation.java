package com.backend.carrental.domain;

import com.backend.carrental.domain.enumeration.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "Please enter the pick up time of the reservation")
    @Column(nullable = false)
    private LocalDateTime pickUpTime;

//    @Temporal(TemporalType.TIMESTAMP)
//    @FutureOrPresent(message = "Please enter valid date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM/dd/yyyy HH:mm:ss", timezone = "Turkey")
    @NotNull(message = "Please enter the drop up time of the reservation")
    @Column(nullable = false)
    private LocalDateTime dropOfTime;

    @Column(length = 50, nullable = false)
    @NotNull(message = "Please enter the pick up location of the reservation")
    private String pickUpLocation;

    @Column(length = 50, nullable = false)
    @NotNull(message = "Please enter the drop of location of the reservation")
    private String dropOfLocation;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private Double totalPrice;

    public Long getTotalHours(LocalDateTime pickUpTime, LocalDateTime dropOfTime) {

        Long seconds = ChronoUnit.SECONDS.between(pickUpTime, dropOfTime);
        Long minutes = ChronoUnit.MINUTES.between(pickUpTime, dropOfTime);
        Long hours = ChronoUnit.HOURS.between(pickUpTime, dropOfTime);
        Long days = ChronoUnit.DAYS.between(pickUpTime, dropOfTime);


        return hours;
    }
}
