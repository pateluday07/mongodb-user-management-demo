package com.user.management.service.user;

import com.user.management.dto.UserDto;
import com.user.management.mapper.UserMapper;
import com.user.management.model.User;
import com.user.management.repository.CarRepository;
import com.user.management.repository.UserRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static com.user.management.constants.ExceptionConstants.*;

/**
 * @author patel
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRespository userRespository;
    private final CarRepository carRepository;

    @Override
    public UserDto saveUser(UserDto userDto) {
        log.info("Service Method To Save User: {}", userDto);
        validateNewUser(userDto);
        return saveUserHelper(userDto);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        log.info("Service Method To Update User: {}", userDto);
        validateExistingUser(userDto);
        return saveUserHelper(userDto);
    }

    @Override
    public UserDto getUserById(String id) {
        log.info("Service Method To Get User By Id: {}", id);
        return userMapper
                .toDtoExceptCar(userRespository
                        .findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG.concat(id))));
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Service Method To Get All Users");
        return userRespository
                .findAll()
                .stream()
                .map(userMapper::toDtoExceptCar)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(String id) {
        log.info("Service Method To Delete User By Id: {}", id);
        User user = userRespository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG.concat(id)));
        if (user.getCars() != null && !user.getCars().isEmpty()) {
            carRepository.deleteAll(user.getCars());
        }
        userRespository
                .delete(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Service Method To Find User By Email: {}", email);
        return userMapper
                .toDtoExceptCar(userRespository
                        .findByContactEmail(email)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Available For The Email: ".concat(email))));
    }

    @Override
    public UserDto getUserByPhone(String phone) {
        log.info("Service Method To Find User By Phone: {}", phone);
        return userMapper
                .toDtoExceptCar(userRespository
                        .findByContactPhone(phone)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Available For The Phone: ".concat(phone))));
    }

    private UserDto saveUserHelper(UserDto userDto) {
        User user = userMapper.toModel(userDto);
        if (user.getCars() != null && !user.getCars().isEmpty()) {
            user.setCars(carRepository.saveAll(user.getCars()));
        }
        return userMapper
                .toDto(userRespository
                        .save(user));
    }

    private void validateNewUser(UserDto userDto) {
        if (StringUtils.isNoneBlank(userDto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New User Should Not Have An Id");
        }
        if (userRespository.existsByContactEmail(userDto.getContact().getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, EMAIL_ALREADY_EXISTS_MSG);
        }
        validatePhone(userDto);
        if (userRespository.existsByContactPhone(userDto.getContact().getPhone())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, PHONE_ALREADY_EXISTS_MSG);
        }
    }

    private void validateExistingUser(UserDto userDto) {
        if (StringUtils.isBlank(userDto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Id Is Not Available");
        }
        if (!userRespository.existsById(userDto.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MSG.concat(userDto.getId()));
        }
        userRespository
                .findByContactEmail(userDto.getContact().getEmail())
                .ifPresent(user -> {
                    if (!user.getId().equals(userDto.getId())) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, EMAIL_ALREADY_EXISTS_MSG);
                    }
                });
        validatePhone(userDto);
        userRespository
                .findByContactPhone(userDto.getContact().getPhone())
                .ifPresent(user -> {
                    if (!user.getId().equals(userDto.getId())) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, PHONE_ALREADY_EXISTS_MSG);
                    }
                });
    }

    private void validatePhone(UserDto userDto) {
        String phone = userDto.getContact().getPhone().trim();
        if (!StringUtils.isNumeric(phone)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Must Have Numbers Only");
        }
        if (phone.length() < 8 || phone.length() > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number Minimum Length Must Be 8 Or Max Length Must Be 12");
        }
    }

}
