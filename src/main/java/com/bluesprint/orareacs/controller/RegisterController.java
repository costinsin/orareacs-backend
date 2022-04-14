package com.bluesprint.orareacs.controller;

import com.bluesprint.orareacs.entity.User;
import com.bluesprint.orareacs.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
@AllArgsConstructor
public class RegisterController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Boolean> addUser(@RequestBody User user) {
        boolean checkAddUser = userService.addUser(user);

        if (!checkAddUser) {
            return new ResponseEntity<>(false, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
