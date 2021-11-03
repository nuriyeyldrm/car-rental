package com.backend.carrental.repository;

import com.backend.carrental.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CarRepository extends JpaRepository<Car, Long> {
}
