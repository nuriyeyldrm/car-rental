package com.backend.carrental.repository;

import com.backend.carrental.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Transactional
    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN fetch r.carId cd " +
            "LEFT JOIN fetch cd.image img")
    List<Reservation> findAllReservation();

    @Transactional
    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN fetch r.carId cd " +
            "LEFT JOIN fetch cd.image img " +
            "LEFT JOIN fetch r.userId uid WHERE r.id = ?1 and uid.id = ?2")
    List<Reservation> findUserReservationsById(Long id, Long userId);

    @Transactional
    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN fetch r.carId cd " +
            "LEFT JOIN fetch cd.image img WHERE r.id = ?1")
    Optional<Reservation> findReservationById(Long id);

    @Transactional
    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN fetch r.carId cd " +
            "LEFT JOIN fetch cd.image img " +
            "LEFT JOIN fetch r.userId uid WHERE r.id = ?1 and uid.id = ?2")
    Optional<Reservation> findReservationByUserId(Long id, Long userId);

    @Transactional
    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN fetch r.carId cd " +
            "LEFT JOIN fetch cd.image img " +
            "LEFT JOIN fetch r.userId uid WHERE uid.id = ?1")
    List<Reservation> findReservationsByUserId(Long userId);

    @Transactional
    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN fetch r.carId cd " +
            "LEFT JOIN fetch cd.image img " +
            "LEFT JOIN fetch r.userId uid WHERE cd.id = ?1 and (?2 BETWEEN r.pickUpTime and r.dropOfTime)")
    Optional<Reservation> checkStatus(Long carId, Date pickUpTime);
}
