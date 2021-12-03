package com.backend.carrental.service;

import com.backend.carrental.domain.Car;
import com.backend.carrental.domain.Reservation;
import com.backend.carrental.domain.User;
import com.backend.carrental.helper.ExcelHelper;
import com.backend.carrental.repository.CarRepository;
import com.backend.carrental.repository.ReservationRepository;
import com.backend.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

@AllArgsConstructor
@Service
public class ExcelService {

    UserRepository userRepository;
    CarRepository carRepository;
    ReservationRepository reservationRepository;

    public ByteArrayInputStream loadUser() {
        List<User> users = userRepository.findAll();

        return ExcelHelper.usersExcel(users);
    }

    public ByteArrayInputStream loadCar() {
        List<Car> cars = carRepository.findAll();

        return ExcelHelper.carsExcel(cars);
    }

    public ByteArrayInputStream loadReservation() {
        List<Reservation> reservations = reservationRepository.findAll();

        return ExcelHelper.reservationsExcel(reservations);
    }
}
