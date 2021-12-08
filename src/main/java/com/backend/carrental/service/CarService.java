package com.backend.carrental.service;

import com.backend.carrental.domain.Car;
import com.backend.carrental.domain.FileDB;
import com.backend.carrental.exception.BadRequestException;
import com.backend.carrental.exception.ResourceNotFoundException;
import com.backend.carrental.repository.CarRepository;
import com.backend.carrental.repository.FileDBRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;

    private final FileDBRepository fileDBRepository;

    private final static String CAR_NOT_FOUND_MSG = "car with id %d not found";

    public List<Car> fetchAllCars(){
        return carRepository.findAllCar();
    }

    public Car findById(Long id) throws ResourceNotFoundException {
        return carRepository.findCarById(id).orElseThrow(() ->
                    new ResourceNotFoundException(String.format(CAR_NOT_FOUND_MSG, id)));
    }

    public void add(Car car, String imageId) throws BadRequestException {
        FileDB fileDB = fileDBRepository.findById(imageId).get();

        Set<FileDB> fileDBs = new HashSet<>();
        fileDBs.add(fileDB);

        car.setImage(fileDBs);
        carRepository.save(car);
    }

    public void updateCar(Long id, Car car, String imageId) throws BadRequestException {

        car.setId(id);
        FileDB fileDB = fileDBRepository.findById(imageId).get();

        Set<FileDB> fileDBs = new HashSet<>();
        fileDBs.add(fileDB);

        car.setImage(fileDBs);

        carRepository.save(car);
    }

    public boolean carAvailability(Long id) throws ResourceNotFoundException {
        // TODO: check car availability

        return true;
    }

    public void removeById(Long id) throws ResourceNotFoundException {
        boolean carExists = carRepository.existsById(id);

        if (!carExists){
            throw new ResourceNotFoundException("car does not exist");
        }

        carRepository.deleteById(id);
    }
}
