package com.user.management.mapper;

import com.user.management.dto.CarDto;
import com.user.management.model.Car;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author patel
 */
@Mapper(componentModel = "spring")
public interface CarMapper {

    Car toModel(CarDto carDto);

    CarDto toDto(Car car);

    List<CarDto> toDto(List<Car> cars);
}
