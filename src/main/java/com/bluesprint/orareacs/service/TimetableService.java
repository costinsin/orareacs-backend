package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.entity.Timetable;

import java.sql.Time;
import java.util.Optional;

public interface TimetableService {
    void addTimetable(Timetable timetable);
    void deleteTimetable(String id);
    Optional<Timetable> findTimetablesById(String id);
}
