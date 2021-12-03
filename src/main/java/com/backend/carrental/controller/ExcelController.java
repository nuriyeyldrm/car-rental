package com.backend.carrental.controller;

import com.backend.carrental.service.ExcelService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("http://localhost:8081")
@AllArgsConstructor
@RestController
@RequestMapping("/excel")
public class ExcelController {

    ExcelService excelService;

    @GetMapping("/download/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> getUserFile() {
        String fileName = "customers.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.loadUser());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
    }

    @GetMapping("/download/cars")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> getCarFile() {
        String fileName = "cars.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.loadCar());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
    }

    @GetMapping("/download/reservations")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> getReservationFile() {
        String fileName = "reservations.xlsx";
        InputStreamResource file = new InputStreamResource(excelService.loadReservation());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
    }
}
