package com.backend.carrental.service;

import com.backend.carrental.domain.Car;
import com.backend.carrental.domain.FileDB;
import com.backend.carrental.exception.BadRequestException;
import com.backend.carrental.repository.CarRepository;
import com.backend.carrental.repository.FileDBRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;

    private final FileDBRepository fileDBRepository;

    public void add(Car car) throws BadRequestException {
        FileDB fileDB = fileDBRepository.findByModel(car.getModel());

        Set<FileDB> fileDBs = new HashSet<>();
        fileDBs.add(fileDB);

        car.setImage(fileDBs);
        carRepository.save(car);
    }
}
