package com.user.management.service.user;

import com.user.management.dto.UserDto;

import java.util.List;

/**
 * @author patel
 */
public interface UserService {

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    UserDto getUserById(String id);

    List<UserDto> getAllUsers();

    void deleteUserById(String id);

    UserDto getUserByEmail(String email);

    UserDto getUserByPhone(String phone);
}
