package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.dto.UserAdminDto;
import com.bluesprint.orareacs.entity.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    void deleteUser(String username);
    UserAdminDto updateUser(UserAdminDto user);
}
