package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.dto.LoginCredentials;
import com.bluesprint.orareacs.dto.UserUpdateDto;
import com.bluesprint.orareacs.entity.User;
import com.bluesprint.orareacs.exception.BadCredentialsException;
import com.bluesprint.orareacs.exception.EmailAlreadyExistsException;
import com.bluesprint.orareacs.exception.UsernameAlreadyExistsException;
import com.bluesprint.orareacs.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private TotpManager totpManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = repository.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        User user = userOptional.get();
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }

    @Override
    @Transactional
    public void addUser(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username: " + user.getUsername() + " already exists!");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email: " + user.getEmail() + " already exists!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRules(new ArrayList<>());
        user.setRole("student");
        user.setTwoFactorSecret(totpManager.generateSecret());
        repository.save(user);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    @Override
    public void checkLoginCredentials(LoginCredentials loginCredentials) {
        Optional<User> userOptional = repository.findUserByUsername(loginCredentials.getUsername());

        if (userOptional.isEmpty()) {
            throw new BadCredentialsException("Invalid username or password!");
        }

        if (!passwordEncoder.matches(loginCredentials.getPassword(), userOptional.get().getPassword())) {
            throw new BadCredentialsException("Invalid username or password!");
        }
    }

    @Override
    public void updateUser(String username, UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = repository.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username!");
        }
        User user = userOptional.get();
        updateUserFields(user, userUpdateDto);

        repository.save(user);
    }

    private void updateUserFields(User user, UserUpdateDto userUpdateDto) {
        if (userUpdateDto.getEmail() != null) {
            if (repository.existsByEmail(userUpdateDto.getEmail())) {
                throw new EmailAlreadyExistsException("Email: " +
                        userUpdateDto.getEmail() + " already exists!");
            }
            user.setEmail(userUpdateDto.getEmail());
        }
        if (userUpdateDto.getGroup() != null) {
            user.setGroup(userUpdateDto.getGroup());
        }
        if (userUpdateDto.getFirstName() != null) {
            user.setFirstName(userUpdateDto.getFirstName());
        }
        if (userUpdateDto.getLastName() != null) {
            user.setLastName(userUpdateDto.getLastName());
        }
        if (userUpdateDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }
    }
}
