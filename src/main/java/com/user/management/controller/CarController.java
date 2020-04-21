package com.user.management.controller;

import com.user.management.dto.CarDto;
import com.user.management.service.car.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author patel
 */
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PutMapping
    public CarDto updateCar(@Valid @RequestBody CarDto carDto) {
        return carService.updateCar(carDto);
    }

    @GetMapping("/by-user-id/{userId}")
    public List<CarDto> getCarsByUserId(@PathVariable String userId) {
        return carService.getCarsByUserId(userId);
    }

    @DeleteMapping("/{carId}")
    public ResponseEntity<String> deleteCarById(@PathVariable String carId) {
        carService.deleteCarById(carId);
        return ResponseEntity.ok("Car Deleted Successfully");
    }
}
