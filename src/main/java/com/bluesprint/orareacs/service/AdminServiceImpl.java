package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.dto.UserAdminDto;
import com.bluesprint.orareacs.entity.User;
import com.bluesprint.orareacs.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String username) {
        Long records = userRepository.deleteByUsername(username);
        if (records == 0) {
            throw new UsernameNotFoundException("User with username " + username + " does not exists!");
        }
    }

    @Override
    public UserAdminDto updateUser(UserAdminDto userDto) {
        Optional<User> userOptional = userRepository.findUserByUsername(userDto.getUsername());
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User with username " + userDto.getUsername() + " does not exists!");
        }

        User user = userOptional.get();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setGroup(userDto.getGroup());
        user.setRole(userDto.getRole());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);

        return userDto;
    }


}
