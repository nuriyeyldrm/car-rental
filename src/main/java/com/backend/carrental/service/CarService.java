package com.backend.carrental.service;

import com.backend.carrental.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;
}
