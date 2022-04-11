package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.entity.User;
import com.bluesprint.orareacs.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository repository;

    @Override
    public boolean addUser(User user) {
        Optional<User> userOptional = repository.findUserByUsername(user.getUsername());

        if (userOptional.isPresent()) {
            return false;
        }
        user.setRules(new ArrayList<>());
        user.setRole("STUDENT");
        repository.save(user);
        return true;
    }
}
