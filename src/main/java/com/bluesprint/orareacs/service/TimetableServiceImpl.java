package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.entity.Timetable;
import com.bluesprint.orareacs.exception.TimetableAlreadyExistsException;
import com.bluesprint.orareacs.exception.TimetableMissingException;
import com.bluesprint.orareacs.repository.TimetableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TimetableServiceImpl implements TimetableService{
    private final TimetableRepository repository;
    @Override
    @Transactional
    public void addTimetable(Timetable timetable) {
        if (repository.existsByGroup(timetable.getGroup())) {
            throw new TimetableAlreadyExistsException("Timetable for group: " + timetable.getGroup() +
                    " already exists!");
        }

        repository.save(timetable);
    }

    @Override
    @Transactional
    public void deleteTimetableByGroup(String group) {
        if (!repository.existsByGroup(group)) {
            throw new TimetableMissingException("Timetable for group: " + group +
                    " does not exist!");
        }

        repository.deleteByGroup(group);
    }
}
