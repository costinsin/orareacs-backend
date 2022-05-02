package com.bluesprint.orareacs.controller;

import com.auth0.jwt.JWT;
import com.bluesprint.orareacs.dto.UserDetailsDto;
import com.bluesprint.orareacs.dto.UserUpdateDto;
import com.bluesprint.orareacs.entity.User;
import com.bluesprint.orareacs.exception.UserAccessNotPermittedException;
import com.bluesprint.orareacs.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

import static com.bluesprint.orareacs.filter.FilterUtils.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('student') or hasAuthority('admin')")
    @GetMapping("/getDetails/{username}")
    public ResponseEntity<?> getDetails(@PathVariable String username,
                                        @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_HEADER.length());
        String tokenUsername = JWT.decode(token).getSubject();
        String role = JWT.decode(token).getClaims().get(ROLES).asList(String.class).get(0);

        if (!tokenUsername.equals(username) && role.equals(STUDENT_ROLE)) {
            throw new UserAccessNotPermittedException("You cannot access other user's details!");
        }

        Optional<User> userOptional = userService.findUserByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        return new ResponseEntity<>(modelMapper.map(userOptional.get(), UserDetailsDto.class), HttpStatus.OK);
    }
}
