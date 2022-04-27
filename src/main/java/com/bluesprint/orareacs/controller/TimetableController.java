package com.bluesprint.orareacs.controller;

import com.bluesprint.orareacs.dto.RegisterResponse;
import com.bluesprint.orareacs.dto.TimetableAddResponse;
import com.bluesprint.orareacs.dto.TimetableDeleteResponse;
import com.bluesprint.orareacs.entity.Timetable;
import com.bluesprint.orareacs.service.TimetableService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/addTimetable")
    public ResponseEntity<?> addTimetable(@RequestBody Timetable timetable) {
        timetableService.addTimetable(timetable);

        TimetableAddResponse response = TimetableAddResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Timetable with id: " + timetable.getId() + " added.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/deleteTimetable")
    public ResponseEntity<?> deleteTimetable(@RequestBody String id) {
        timetableService.deleteTimetable(id);

        TimetableDeleteResponse response = TimetableDeleteResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Timetable with id: " + id + " deleted.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
