package com.bluesprint.orareacs.controller;

import com.auth0.jwt.JWT;
import com.bluesprint.orareacs.dto.Event;
import com.bluesprint.orareacs.dto.TimetableAddResponse;
import com.bluesprint.orareacs.dto.TimetableDeleteResponse;
import com.bluesprint.orareacs.entity.Course;
import com.bluesprint.orareacs.entity.Timetable;
import com.bluesprint.orareacs.service.TimetableService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bluesprint.orareacs.filter.FilterUtils.TOKEN_HEADER;

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

    @PreAuthorize("hasAuthority('student')")
    @GetMapping("/timetable")
    public ResponseEntity<?> getTimetable(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_HEADER.length());
        String username = JWT.decode(token).getSubject();
        List<Event> events = timetableService.getTimetable(username);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('student')")
    @GetMapping("/course")
    public ResponseEntity<?> getCourse(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(TOKEN_HEADER.length());
        String username = JWT.decode(token).getSubject();
        List<Course> courses = timetableService.getCourses(username);

        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/getGroups")
    public ResponseEntity<?> getGroups() {
        List<String> groups = timetableService.getGroups();
        if (groups == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
}
