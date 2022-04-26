package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.entity.Timetable;
import com.bluesprint.orareacs.exception.TimetableAlreadyExistsException;
import com.bluesprint.orareacs.repository.TimetableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TimetableServiceImpl implements TimetableService{
    private final TimetableRepository repository;
    @Override
    public void addTimetable(Timetable timetable) {
        if (repository.existsByGroup(timetable.getGroup())) {
            throw new TimetableAlreadyExistsException("Group: " + timetable.getGroup() + " already exists!");
        }

        timetable.setGroup(timetable.getGroup());
        timetable.setCourseList(timetable.getCourseList());
        repository.save(timetable);
    }

    @Override
    public Optional<Timetable> findTimetableByGroup(String group) {
        return repository.findTimetableByGroup(group);
    }
}
