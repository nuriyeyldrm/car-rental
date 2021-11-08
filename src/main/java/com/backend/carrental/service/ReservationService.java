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

    public Reservation findByUserId(Long id, String username) throws ResourceNotFoundException {
        return reservationRepository.findReservationByUserId(id, username).orElseThrow(() ->
                new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG, id)));
    }

    public List<Reservation> findAllByUserId(String username) throws ResourceNotFoundException {
        return reservationRepository.findReservationsByUserId(username);
    }

    public void addReservation(Reservation reservation, String userName, Car carId) throws BadRequestException {
        reservation.setStatus(true);
        reservation.setCarId(carId);
        Optional<User> user = userRepository.findByUsername(userName);
        reservation.setUserId(user.get());
        reservationRepository.save(reservation);
    }

    public void updateReservation(Car carId, Long id, Reservation reservation) throws BadRequestException {

        boolean reservationExist = reservationRepository.findById(id).isPresent();

        if (!reservationExist){
            throw new ConflictException("Error: Reservation does not exist!");
        }

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
}
