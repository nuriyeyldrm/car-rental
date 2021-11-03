package com.backend.carrental.repository;

import com.backend.carrental.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
