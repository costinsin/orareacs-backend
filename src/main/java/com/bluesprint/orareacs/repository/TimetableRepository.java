package com.bluesprint.orareacs.repository;

import com.bluesprint.orareacs.entity.Timetable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TimetableRepository extends MongoRepository<Timetable, String> {
    Optional<Timetable> findTimetableByGroup(String group);
    boolean existsByGroup(String group);
}
