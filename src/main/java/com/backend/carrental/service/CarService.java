package com.backend.carrental.service;

import com.backend.carrental.domain.Car;
import com.backend.carrental.domain.FileDB;
import com.backend.carrental.dto.CarDTO;
import com.backend.carrental.exception.BadRequestException;
import com.backend.carrental.exception.ResourceNotFoundException;
import com.backend.carrental.repository.CarRepository;
import com.backend.carrental.repository.FileDBRepository;
import com.backend.carrental.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class CarService {

    private final CarRepository carRepository;

    private final ReservationRepository reservationRepository;

    private final FileDBRepository fileDBRepository;

    private final static String CAR_NOT_FOUND_MSG = "car with id %d not found";

    private final static String IMAGE_NOT_FOUND_MSG = "car with id %s not found";

    public List<CarDTO> fetchAllCars(){
        return carRepository.findAllCar();
    }

    public CarDTO findById(Long id) throws ResourceNotFoundException {
        return carRepository.findCarByIdx(id).orElseThrow(() ->
                    new ResourceNotFoundException(String.format(CAR_NOT_FOUND_MSG, id)));
    }

    public void add(Car car, String imageId) throws BadRequestException {
        FileDB fileDB = fileDBRepository.findById(imageId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(IMAGE_NOT_FOUND_MSG, imageId)));

        Set<FileDB> fileDBs = new HashSet<>();
        fileDBs.add(fileDB);

        car.setImage(fileDBs);
        car.setBuiltIn(false);
        carRepository.save(car);
    }

    public void updateCar(Long id, Car car, String imageId) throws BadRequestException {
        car.setId(id);
        FileDB fileDB = fileDBRepository.findById(imageId).get();

        Car car1 = carRepository.getById(id);

        if (car1.getBuiltIn())
            throw new BadRequestException("You dont have permission to update car!");

        car.setBuiltIn(false);

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
        Car car = carRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(CAR_NOT_FOUND_MSG, id)));

        if (car.getBuiltIn())
            throw new BadRequestException("You dont have permission to delete car!");

        boolean reservationExist = reservationRepository.existsByCarId(car);

        if (reservationExist){
            throw new ResourceNotFoundException("Reservation(s) exist for car!");
        }

        carRepository.deleteById(id);
    }
}
