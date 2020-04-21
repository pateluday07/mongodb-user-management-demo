package com.user.management.service.car;

import com.user.management.dto.CarDto;

import java.util.List;

/**
 * @author patel
 */
public interface CarService {

    CarDto updateCar(CarDto carDto);

    List<CarDto> getCarsByUserId(String userId);

    void deleteCarById(String carId);
}
