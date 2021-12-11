package com.backend.carrental.service;

import com.backend.carrental.domain.Car;
import com.backend.carrental.domain.Reservation;
import com.backend.carrental.domain.User;
import com.backend.carrental.domain.enumeration.ReservationStatus;
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

    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";

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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));

        if (!checkStatus)
            reservation.setStatus(ReservationStatus.CREATED);
        else
            throw new BadRequestException("Car is already reserved! Please choose another");

        reservation.setCarId(carId);
        reservation.setUserId(user);

        Double totalPrice = totalPrice(reservation.getPickUpTime(), reservation.getDropOfTime(), carId.getId());
        reservation.setTotalPrice(totalPrice);

        reservationRepository.save(reservation);
    }

    public void updateReservation(Car carId, Long id, Reservation reservation) throws BadRequestException {

        boolean checkStatus = carAvailability(carId.getId(), reservation.getPickUpTime(), reservation.getDropOfTime());

        Optional<Reservation> reservationExist = reservationRepository.findById(id);

        if (!(reservationExist.isPresent())){
            throw new ConflictException("Error: Reservation does not exist!");
        }

        if (reservation.getPickUpTime().compareTo(reservationExist.get().getPickUpTime()) == 0 &&
                reservation.getDropOfTime().compareTo(reservationExist.get().getDropOfTime()) == 0)
            System.out.println();
        else if (checkStatus)
            throw new BadRequestException("Car is already reserved! Please choose another");

        Double totalPrice = totalPrice(reservation.getPickUpTime(), reservation.getDropOfTime(), carId.getId());
        reservationExist.get().setTotalPrice(totalPrice);

        reservationExist.get().setCarId(carId);
        reservationExist.get().setPickUpTime(reservation.getPickUpTime());
        reservationExist.get().setDropOfTime(reservation.getDropOfTime());
        reservationExist.get().setPickUpLocation(reservation.getPickUpLocation());
        reservationExist.get().setDropOfLocation(reservation.getDropOfLocation());
        reservationExist.get().setStatus(reservation.getStatus());

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
        List<Reservation> checkStatus = reservationRepository.checkStatus(carId, pickUpTime, dropOffTime,
                ReservationStatus.DONE, ReservationStatus.CANCELED);
        return checkStatus.size() > 0;
    }

    public Double totalPrice(LocalDateTime pickUpTime, LocalDateTime dropOfTime, Long carId) {
        Optional<Car> car = carRepository.findCarById(carId);
        Long hours = (new Reservation()).getTotalHours(pickUpTime, dropOfTime);
        return car.get().getPricePerHour() * hours;
    }
}