package com.backend.carrental.repository;

import com.backend.carrental.domain.Car;
import com.backend.carrental.dto.CarDTO;
import com.backend.carrental.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CarRepository extends JpaRepository<Car, Long> {

    @Transactional
    @Query("SELECT new com.backend.carrental.dto.CarDTO(c) FROM Car c")
    List<CarDTO> findAllCar();

    @Transactional
    @Query("SELECT new com.backend.carrental.dto.CarDTO(c) FROM Car c WHERE c.id = ?1")
    Optional<CarDTO> findCarByIdx(Long id) throws ResourceNotFoundException;

    @Transactional
    @Query("SELECT c FROM Car c " +
            "LEFT JOIN fetch c.image img WHERE c.id = ?1")
    Optional<Car> findCarById(Long id) throws ResourceNotFoundException;
}
