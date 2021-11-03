package com.backend.carrental.controller;

import com.backend.carrental.domain.Car;
import com.backend.carrental.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/api/car")
public class CarController {

    public CarService carService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> addCar(@Valid @RequestBody Car car) {
        carService.add(car);
        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }
}
