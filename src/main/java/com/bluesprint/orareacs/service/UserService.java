package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.dto.LoginCredentials;
import com.bluesprint.orareacs.dto.UserUpdateDto;
import com.bluesprint.orareacs.entity.User;

import java.util.Optional;

public interface UserService {
    void addUser(User user);

    Optional<User> findUserByUsername(String username);

    void checkLoginCredentials(LoginCredentials loginCredentials);

    void updateUser(String username, UserUpdateDto userUpdateDto);
}
