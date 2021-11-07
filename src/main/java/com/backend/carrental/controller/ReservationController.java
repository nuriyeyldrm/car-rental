package com.backend.carrental.controller;

import com.backend.carrental.domain.Car;
import com.backend.carrental.domain.Reservation;
import com.backend.carrental.domain.User;
import com.backend.carrental.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/api/reservations")
public class ReservationController {

    public ReservationService reservationService;

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Reservation>> getAllUserReservations(HttpServletRequest request){
        Long id = (Long) request.getAttribute("id");
        List<Reservation> reservations = reservationService.fetchUserReservationsById(id);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getAllReservations(){
        List<Reservation> reservations = reservationService.fetchAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id){
        Reservation reservation = reservationService.findById(id);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Reservation> getUserReservationById(HttpServletRequest request){
        Long id = (Long) request.getAttribute("id");
        Reservation reservation = reservationService.findById(id);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Map<String, Boolean>> makeReservation(HttpServletRequest request,
                                                                @RequestParam (value = "car_id") Car carId,
                                                                @Valid @RequestBody Reservation reservation) {
        String username = (String) request.getAttribute("username");
        reservationService.addReservation(reservation, username, carId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> updateReservation(@PathVariable Long id,
                                                                  @Valid @RequestBody Reservation reservation) {
        reservationService.updateReservation(id, reservation);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> deleteReservation(@PathVariable Long id){
        reservationService.removeById(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
