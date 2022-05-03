package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.dto.Event;
import com.bluesprint.orareacs.entity.Timetable;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

public interface TimetableService {
    void addTimetable(Timetable timetable);
    void deleteTimetableByGroup(String group);

    List<Event> getTimetable(String username);
}
