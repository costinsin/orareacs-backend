package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.dto.Event;
import com.bluesprint.orareacs.entity.Course;
import com.bluesprint.orareacs.entity.Timetable;

import java.util.List;

public interface TimetableService {
    void addTimetable(Timetable timetable);

    void deleteTimetableByGroup(String group);

    List<Event> getTimetable(String username);

    List<Course> getCourses(String username);

    List<String> getGroups();
}
