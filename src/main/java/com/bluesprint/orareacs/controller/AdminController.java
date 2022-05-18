package com.bluesprint.orareacs.controller;

import com.bluesprint.orareacs.dto.UserAdminDto;
import com.bluesprint.orareacs.service.AdminService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AdminController {
    public final AdminService adminService;
    private final ModelMapper modelMapper;

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {

        List<UserAdminDto> userDtos = adminService.getAllUsers()
                .stream()
                .map(user -> modelMapper.map(user, UserAdminDto.class))
                .toList();

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@RequestBody UserAdminDto userAdminDto) {
        adminService.deleteUser(userAdminDto.getUsername());
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("status", Integer.toString(HttpStatus.OK.value()));
        successResponse.put("message", "User successfully deleted!");

        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody UserAdminDto userAdminDto) {
        return new ResponseEntity<>(adminService.updateUser(userAdminDto), HttpStatus.OK);
    }
}
