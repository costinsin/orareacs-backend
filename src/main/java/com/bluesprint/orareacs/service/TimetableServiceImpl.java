package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.entity.Timetable;
import com.bluesprint.orareacs.exception.TimetableAlreadyExistsException;
import com.bluesprint.orareacs.exception.TimetableMissing;
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
        if (repository.existsById(timetable.getId())) {
            throw new TimetableAlreadyExistsException("Group: " + timetable.getGroup() + " already exists!");
        }

        timetable.setId(timetable.getId());
        timetable.setGroup(timetable.getGroup());
        timetable.setCourseList(timetable.getCourseList());
        repository.save(timetable);
    }

    @Override
    public void deleteTimetable(String id) {
        var timetableFromId = findTimetablesById(id);
        if (timetableFromId.isPresent()) {
            repository.delete(timetableFromId.get());
        } else {
            throw new TimetableMissing("Timetable with id: " + id + " doesn't exist!");
        }
    }

    @Override
    public Optional<Timetable> findTimetablesById(String id) {
        return repository.findTimetablesById(id);
    }
}
