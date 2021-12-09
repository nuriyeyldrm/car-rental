package com.backend.carrental.service;

import com.backend.carrental.domain.Car;
import com.backend.carrental.domain.Reservation;
import com.backend.carrental.domain.User;
import com.backend.carrental.dto.ReservationDTO;
import com.backend.carrental.exception.BadRequestException;
import com.backend.carrental.exception.ConflictException;
import com.backend.carrental.exception.ResourceNotFoundException;
import com.backend.carrental.repository.CarRepository;
import com.backend.carrental.repository.ReservationRepository;
import com.backend.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final CarRepository carRepository;

    private final static String RESERVATION_NOT_FOUND_MSG = "reservation with id %d not found";

    public List<ReservationDTO> fetchUserReservationsById(Long id, Long userId){
        return reservationRepository.findUserReservationsById(id, userId);
    }

    public List<ReservationDTO> fetchAllReservations(){
        return reservationRepository.findAllReservation();
    }

    public ReservationDTO findById(Long id) throws ResourceNotFoundException {
        return reservationRepository.findReservationById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG, id)));
    }

    public ReservationDTO findByUserId(Long id, Long userId) throws ResourceNotFoundException {
        return reservationRepository.findReservationByUserId(id, userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG, id)));
    }

    public List<ReservationDTO> findAllByUserId(Long id) throws ResourceNotFoundException {
        return reservationRepository.findReservationsByUserId(id);
    }

    public void addReservation(Reservation reservation, Long id, Car carId) throws BadRequestException {
        boolean checkStatus = carAvailability(carId.getId(), reservation.getPickUpTime(), reservation.getDropOfTime());
        Optional<Car> car = carRepository.findCarById(carId.getId());

        if (!checkStatus)
            reservation.setStatus(true);
        else
            throw new BadRequestException("Car is already reserved! Please choose another");

        reservation.setCarId(carId);
        Optional<User> user = userRepository.findById(id);
        reservation.setUserId(user.get());

        long hours = reservation.getTotalHours();
        Double totalPrice = car.get().getPricePerHour() * hours;
        reservation.setTotalPrice(totalPrice);

        reservationRepository.save(reservation);
    }

    public void updateReservation(Car carId, Long id, Reservation reservation) throws BadRequestException {

        Optional<Reservation> reservationExist = reservationRepository.findById(id);
        Optional<Car> car = carRepository.findCarById(carId.getId());

        if (!(reservationExist.isPresent())){
            throw new ConflictException("Error: Reservation does not exist!");
        }

        boolean checkStatus = carAvailability(carId.getId(), reservation.getPickUpTime(), reservation.getDropOfTime());

        if (!checkStatus)
            reservation.setStatus(true);
        else
            throw new BadRequestException("Car is already reserved! Please choose another");

        long hours = reservation.getTotalHours();
        Double totalPrice = car.get().getPricePerHour() * hours;
        reservationExist.get().setTotalPrice(totalPrice);

        reservationExist.get().setCarId(carId);
        reservationExist.get().setPickUpTime(reservation.getPickUpTime());
        reservationExist.get().setDropOfTime(reservation.getDropOfTime());
        reservationExist.get().setPickUpLocation(reservation.getPickUpLocation());
        reservationExist.get().setDropOfLocation(reservation.getDropOfLocation());

        reservationRepository.save(reservationExist.get());
    }

    public void removeById(Long id) throws ResourceNotFoundException {
        boolean reservationExists = reservationRepository.existsById(id);

        if (!reservationExists){
            throw new ResourceNotFoundException("reservation does not exist");
        }

        reservationRepository.deleteById(id);
    }

    public boolean carAvailability(Long carId, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        Optional<Reservation> checkStatus = reservationRepository.checkStatus(carId, pickUpTime, dropOffTime);
        return checkStatus.isPresent();
    }
}
