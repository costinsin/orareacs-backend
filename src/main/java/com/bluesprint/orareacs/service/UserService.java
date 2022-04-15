package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.entity.User;

import java.util.Optional;

public interface UserService {
    boolean addUser(User user);
    Optional<User> findUserByUsername(String username);
}
