package com.bluesprint.orareacs.controller;

import com.bluesprint.orareacs.dto.TimetableAddResponse;
import com.bluesprint.orareacs.dto.TimetableDeleteResponse;
import com.bluesprint.orareacs.entity.Timetable;
import com.bluesprint.orareacs.service.TimetableService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;

    @PreAuthorize("hasAuthority('admin')")
    @PostMapping("/timetable")
    public ResponseEntity<?> addTimetable(@RequestBody Timetable timetable) {
        timetableService.addTimetable(timetable);

        TimetableAddResponse response = TimetableAddResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Timetable for group: " + timetable.getGroup() + " added.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/timetable/{group}")
    public ResponseEntity<?> deleteTimetable(@PathVariable String group) {
        timetableService.deleteTimetableByGroup(group);

        TimetableDeleteResponse response = TimetableDeleteResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Timetable for group: " + group + " deleted.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
