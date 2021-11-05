package com.backend.carrental.repository;

import com.backend.carrental.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Transactional
    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN fetch r.carId cd" +
            "LEFT JOIN fetch cdLEFT.image img")
    List<Reservation> findAllReservation();

    @Transactional
    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN fetch r.carId cd" +
            "LEFT JOIN fetch cdLEFT.image img WHERE r.id = ?1")
    Optional<Reservation> findReservationById(Long id);
}
