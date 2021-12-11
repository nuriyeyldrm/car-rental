package com.backend.carrental.repository;

import com.backend.carrental.domain.Car;
import com.backend.carrental.domain.Reservation;
import com.backend.carrental.domain.User;
import com.backend.carrental.domain.enumeration.ReservationStatus;
import com.backend.carrental.dto.ReservationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByUserId(User user);

    boolean existsByCarId(Car car);

    @Transactional
    @Query("SELECT new com.backend.carrental.dto.ReservationDTO(r) FROM Reservation r")
    List<ReservationDTO> findAllReservation();

    @Transactional
    @Query("SELECT new com.backend.carrental.dto.ReservationDTO(r) FROM Reservation r  WHERE r.id = ?1 and r.userId.id = ?2")
    List<ReservationDTO> findUserReservationsById(Long id, Long userId);

    @Transactional
    @Query("SELECT new com.backend.carrental.dto.ReservationDTO(r) FROM Reservation r WHERE r.id = ?1")
    Optional<ReservationDTO> findReservationById(Long id);

    @Transactional
    @Query("SELECT new com.backend.carrental.dto.ReservationDTO(r) FROM Reservation r WHERE r.id = ?1 and r.userId.id = ?2")
    Optional<ReservationDTO> findReservationByUserId(Long id, Long userId);

    @Transactional
    @Query("SELECT new com.backend.carrental.dto.ReservationDTO(r) FROM Reservation r WHERE r.userId.id = ?1")
    List<ReservationDTO> findReservationsByUserId(Long userId);

    @Transactional
    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN fetch r.carId cd " +
            "LEFT JOIN fetch cd.image img " +
            "LEFT JOIN fetch r.userId uid WHERE " +
            "(cd.id = ?1 and r.status <> ?4 and r.status <> ?5 and ?2 BETWEEN r.pickUpTime and r.dropOfTime) or " +
            "(cd.id = ?1 and r.status <> ?4 and r.status <> ?5 and ?3 BETWEEN r.pickUpTime and r.dropOfTime)")
            List<Reservation> checkStatus(Long carId, LocalDateTime pickUpTime, LocalDateTime dropOffTime,
                                              ReservationStatus done, ReservationStatus canceled);
}
