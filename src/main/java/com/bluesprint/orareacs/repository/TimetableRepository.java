package com.bluesprint.orareacs.repository;

import com.bluesprint.orareacs.entity.Timetable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends MongoRepository<Timetable, String> {
    boolean existsByGroup(String group);

    void deleteByGroup(String group);

    Optional<Timetable> getTimetableByGroup(String group);
}
