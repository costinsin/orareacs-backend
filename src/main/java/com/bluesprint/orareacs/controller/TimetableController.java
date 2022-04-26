package com.bluesprint.orareacs.controller;

import com.bluesprint.orareacs.dto.RegisterResponse;
import com.bluesprint.orareacs.entity.Timetable;
import com.bluesprint.orareacs.service.TimetableService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;

    @PostMapping("/add/orar")
    public ResponseEntity<?> addTimetable(@RequestBody Timetable timetable) {
        timetableService.addTimetable(timetable);

        RegisterResponse response = RegisterResponse.builder()
                .status(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
