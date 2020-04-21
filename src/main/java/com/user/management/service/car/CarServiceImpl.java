package com.user.management.service.car;

import com.user.management.dto.CarDto;
import com.user.management.mapper.CarMapper;
import com.user.management.model.User;
import com.user.management.repository.CarRepository;
import com.user.management.repository.UserRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static com.user.management.constants.ExceptionConstants.CAR_NOT_FOUND_MSG;
import static com.user.management.constants.ExceptionConstants.USER_NOT_FOUND_MSG;

/**
 * @author patel
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final UserRespository userRespository;
    private final CarMapper carMapper;

    @Override
    public CarDto updateCar(CarDto carDto) {
        log.info("Service Method To Update Car: {}", carDto);
        if (StringUtils.isBlank(carDto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car Id Is Not Available");
        }
        if (Boolean.FALSE.equals(carRepository.existsCarById(carDto.getId()))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car Not Available For The Id: ".concat(carDto.getId()));
        }
        return carMapper.toDto(carRepository.save(carMapper.toModel(carDto)));
    }

    @Override
    public List<CarDto> getCarsByUserId(String userId) {
        log.info("Service Method To Get Cars By User Id: {}", userId);
        User user = userRespository
                .findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG
                        .concat(userId)));
        if (user.getCars() == null || user.getCars().isEmpty()) {
            return Collections.emptyList();
        }
        return carMapper
                .toDto(user.getCars());
    }

    @Override
    public void deleteCarById(String carId) {
        log.info("Service Method To Delete Car By Id: {}", carId);
        carRepository.delete(carRepository
                .findById(carId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, CAR_NOT_FOUND_MSG.concat(carId))));
    }


}
