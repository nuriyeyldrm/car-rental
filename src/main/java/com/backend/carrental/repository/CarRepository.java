package com.backend.carrental.repository;

import com.backend.carrental.domain.Car;
import com.backend.carrental.exception.ConflictException;
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
    @Query("SELECT c FROM Car c " +
            "LEFT JOIN fetch c.image img")
    List<Car> findAllCar();

    @Transactional
    @Query("SELECT c FROM Car c " +
            "LEFT JOIN fetch c.image img WHERE c.id = ?1")
    Optional<Car> findCarById(Long id) throws ResourceNotFoundException;

    Boolean existsByModel(String model) throws ConflictException;
}
