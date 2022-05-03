package com.bluesprint.orareacs.controller;

import com.auth0.jwt.JWT;
import com.bluesprint.orareacs.dto.UserDetailsDto;
import com.bluesprint.orareacs.dto.UserUpdateDto;
import com.bluesprint.orareacs.entity.User;
import com.bluesprint.orareacs.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.bluesprint.orareacs.filter.FilterUtils.TOKEN_HEADER;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class StudentController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('student')")
    @GetMapping("/getDetails")
    public ResponseEntity<?> getDetails(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_HEADER.length());
        String tokenUsername = JWT.decode(token).getSubject();

        Optional<User> userOptional = userService.findUserByUsername(tokenUsername);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        return new ResponseEntity<>(modelMapper.map(userOptional.get(), UserDetailsDto.class), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('student')")
    @PutMapping("/updateDetails")
    public ResponseEntity<?> updateDetails(@RequestBody UserUpdateDto userDetails,
                                           @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_HEADER.length());
        String tokenUsername = JWT.decode(token).getSubject();

        userService.updateUser(tokenUsername, userDetails);
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("status", Integer.toString(HttpStatus.OK.value()));
        successResponse.put("message", "User successfully updated!");
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

}
