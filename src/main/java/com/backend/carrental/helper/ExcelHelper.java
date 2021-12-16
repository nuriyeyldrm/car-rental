package com.backend.carrental.helper;

import com.backend.carrental.domain.Car;
import com.backend.carrental.domain.Reservation;
import com.backend.carrental.domain.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERS_USER = { "Id", "FirstName", "LastName", "PhoneNumber", "Email", "Address", "ZipCode", "Roles" };
    static String SHEET_USER = "Customers";

    static String[] HEADERS_CAR = { "Id", "Model", "Doors", "Seats", "Luggage", "Transmission", "AirConditioning",
            "Age", "pricePerDay", "FuelType" };
    static String SHEET_CAR = "Cars";

    static String[] HEADERS_RESERVATION = { "Id", "CarId", "CarModel", "CustomerId", "CustomerFullName",
            "CustomerPhone", "PickUpTime", "DropOfTime", "PickUpLocation", "DropOfLocation", "Status" };
    static String SHEET_RESERVATION = "Reservations";

    public static ByteArrayInputStream usersExcel(List<User> users)  {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET_USER);
            Row headerRow = sheet.createRow(0);

            for (int column = 0; column < HEADERS_USER.length; column++) {
                Cell cell = headerRow.createCell(column);
                cell.setCellValue(HEADERS_USER[column]);
            }

            int rowId = 1;
            for (User user: users) {
                Row row = sheet.createRow(rowId++);

                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getFirstName());
                row.createCell(2).setCellValue(user.getLastName());
                row.createCell(3).setCellValue(user.getPhoneNumber());
                row.createCell(4).setCellValue(user.getEmail());
                row.createCell(5).setCellValue(user.getAddress());
                row.createCell(6).setCellValue(user.getZipCode());
                row.createCell(7).setCellValue(user.getRoles().toString());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }

    }

    public static ByteArrayInputStream carsExcel(List<Car> cars)  {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET_CAR);
            Row headerRow = sheet.createRow(0);

            for (int column = 0; column < HEADERS_CAR.length; column++) {
                Cell cell = headerRow.createCell(column);
                cell.setCellValue(HEADERS_CAR[column]);
            }

            int rowId = 1;
            for (Car car: cars) {
                Row row = sheet.createRow(rowId++);

                row.createCell(0).setCellValue(car.getId());
                row.createCell(1).setCellValue(car.getModel());
                row.createCell(2).setCellValue(car.getDoors());
                row.createCell(3).setCellValue(car.getSeats());
                row.createCell(4).setCellValue(car.getLuggage());
                row.createCell(5).setCellValue(car.getTransmission());
                row.createCell(6).setCellValue(car.getAirConditioning());
                row.createCell(7).setCellValue(car.getAge());
                row.createCell(8).setCellValue(car.getPricePerHour());
                row.createCell(9).setCellValue(car.getFuelType());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }

    }

    public static ByteArrayInputStream reservationsExcel(List<Reservation> reservations)  {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET_RESERVATION);
            Row headerRow = sheet.createRow(0);

            for (int column = 0; column < HEADERS_RESERVATION.length; column++) {
                Cell cell = headerRow.createCell(column);
                cell.setCellValue(HEADERS_RESERVATION[column]);
            }

            int rowId = 1;
            for (Reservation reservation: reservations) {
                Row row = sheet.createRow(rowId++);

                row.createCell(0).setCellValue(reservation.getId());
                row.createCell(1).setCellValue(reservation.getCarId().getId());
                row.createCell(2).setCellValue(reservation.getCarId().getModel());
                row.createCell(3).setCellValue(reservation.getUserId().getId());
                row.createCell(4).setCellValue(reservation.getUserId().getFullName());
                row.createCell(5).setCellValue(reservation.getUserId().getPhoneNumber());
                row.createCell(6).setCellValue(reservation.getPickUpTime().toString());
                row.createCell(7).setCellValue(reservation.getDropOfTime().toString());
                row.createCell(8).setCellValue(reservation.getPickUpLocation());
                row.createCell(9).setCellValue(reservation.getDropOfLocation());
                row.createCell(10).setCellValue(reservation.getStatus().toString());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }

    }
}
