package com.backend.carrental.service;

import com.backend.carrental.domain.Car;
import com.backend.carrental.domain.Reservation;
import com.backend.carrental.domain.User;
import com.backend.carrental.exception.BadRequestException;
import com.backend.carrental.exception.ConflictException;
import com.backend.carrental.exception.ResourceNotFoundException;
import com.backend.carrental.repository.ReservationRepository;
import com.backend.carrental.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final static String RESERVATION_NOT_FOUND_MSG = "reservation with id %d not found";

    public List<Reservation> fetchUserReservationsById(Long id, Long userId){
        return reservationRepository.findUserReservationsById(id, userId);
    }

    public List<Reservation> fetchAllReservations(){
        return reservationRepository.findAllReservation();
    }

    public Reservation findById(Long id) throws ResourceNotFoundException {
        return reservationRepository.findReservationById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG, id)));
    }

    public Reservation findByUserId(Long id, Long userId) throws ResourceNotFoundException {
        return reservationRepository.findReservationByUserId(id, userId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG, id)));
    }

    public List<Reservation> findAllByUserId(Long id) throws ResourceNotFoundException {
        return reservationRepository.findReservationsByUserId(id);
    }

    public void addReservation(Reservation reservation, Long id, Car carId) throws BadRequestException {
        boolean checkStatus = carAvailability(carId.getId(), reservation.getPickUpTime());
        if (!checkStatus)
            reservation.setStatus(true);
        else
            throw new BadRequestException("Car is already reserved! Please choose another");
        reservation.setCarId(carId);
        Optional<User> user = userRepository.findById(id);
        reservation.setUserId(user.get());
        reservationRepository.save(reservation);
    }

    public void updateReservation(Car carId, Long id, Reservation reservation) throws BadRequestException {

        boolean reservationExist = reservationRepository.findById(id).isPresent();

        if (!reservationExist){
            throw new ConflictException("Error: Reservation does not exist!");
        }

        boolean checkStatus = carAvailability(carId.getId(), reservation.getPickUpTime());
        if (checkStatus)
            reservation.setStatus(true);
        else
            throw new BadRequestException("Car is already reserved! Please choose another");

        reservation.setId(id);
        reservation.setCarId(carId);
        reservationRepository.save(reservation);
    }

    public void removeById(Long id) throws ResourceNotFoundException {
        boolean reservationExists = reservationRepository.existsById(id);

        if (!reservationExists){
            throw new ResourceNotFoundException("reservation does not exist");
        }

        reservationRepository.deleteById(id);
    }

    public boolean carAvailability(Long carId, Date date) {
        Optional<Reservation> checkStatus = reservationRepository.checkStatus(carId, date);
        return checkStatus.isPresent();
    }
}
