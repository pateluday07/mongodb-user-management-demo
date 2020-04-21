package com.user.management.mapper;

import com.user.management.dto.UserDto;
import com.user.management.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author patel
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    User toModel(UserDto userDto);

    UserDto toDto(User user);

    @Mapping(target = "cars", ignore = true)
    UserDto toDtoExceptCar(User user);
}
