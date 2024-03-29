package com.bluesprint.orareacs.service;

import com.bluesprint.orareacs.dto.Event;
import com.bluesprint.orareacs.entity.Course;
import com.bluesprint.orareacs.entity.Rule;
import com.bluesprint.orareacs.entity.Timetable;
import com.bluesprint.orareacs.entity.User;
import com.bluesprint.orareacs.exception.TimetableAlreadyExistsException;
import com.bluesprint.orareacs.exception.TimetableMissingException;
import com.bluesprint.orareacs.repository.TimetableRepository;
import com.bluesprint.orareacs.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class TimetableServiceImpl implements TimetableService {
    private final TimetableRepository timetableRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addTimetable(Timetable timetable) {
        if (timetableRepository.existsByGroup(timetable.getGroup())) {
            throw new TimetableAlreadyExistsException("Timetable for group: " + timetable.getGroup() +
                    " already exists!");
        }

        timetable.getCourses().forEach(course -> course.setId(UUID.randomUUID().toString()));
        timetableRepository.save(timetable);
    }

    @Override
    @Transactional
    public void updateTimetable(Timetable timetable) {
        if (timetable.getId() == null) {
            throw new TimetableMissingException("Timetable should contain id.");
        }
        timetableRepository.save(timetable);
    }

    @Override
    @Transactional
    public void deleteTimetableByGroup(String group) {
        if (!timetableRepository.existsByGroup(group)) {
            throw new TimetableMissingException("Timetable for group: " + group +
                    " does not exist!");
        }

        timetableRepository.deleteByGroup(group);

        userRepository.getAllByGroup(group).forEach(
                user -> {
                    user.setRules(user.getRules().stream()
                            .filter(rule -> rule.getType().equalsIgnoreCase("add"))
                            .toList());
                    userRepository.save(user);
                }
        );
    }

    @Override
    public List<Course> getCourses(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        User user = userOptional.get();
        Optional<Timetable> optionalTimetable = timetableRepository.getTimetableByGroup(user.getGroup());
        if (optionalTimetable.isEmpty()) {
            throw new TimetableMissingException("Timetable for group " + user.getGroup() +
                    " does not exist!");
        }
        Timetable timetable = optionalTimetable.get();

        return timetable.getCourses();
    }

    @Override
    public List<String> getGroups() {
        List<Timetable> timetableList = timetableRepository.findAll();

        return timetableList.stream()
                .map(Timetable::getGroup)
                .sorted()
                .toList();
    }

    @Override
    public List<Event> getTimetable(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        User user = userOptional.get();
        Optional<Timetable> optionalTimetable = timetableRepository.getTimetableByGroup(user.getGroup());
        if (optionalTimetable.isEmpty()) {
            throw new TimetableMissingException("Timetable for group " + user.getGroup() +
                    " does not exist!");
        }
        Timetable timetable = optionalTimetable.get();

        List<Rule> addRules = user.getRules().stream()
                .filter(rule -> rule.getType().equalsIgnoreCase("add"))
                .toList();
        List<Rule> removeRules = user.getRules().stream()
                .filter(rule -> rule.getType().equalsIgnoreCase("remove"))
                .toList();

        List<Event> timetableEvents = timetable.getCourses().stream()
                .map(course -> processRemoveRule(removeRules, course, timetable))
                .flatMap(List::stream)
                .toList();
        List<Event> addRuleEvents = addRules.stream()
                .map(rule -> getEventsFromAddRule(timetable, rule))
                .flatMap(List::stream)
                .toList();

        List<Event> result = new ArrayList<>(addRuleEvents);
        result.addAll(timetableEvents);

        return result;
    }

    @Override
    public Timetable getTimetableByGroup(String group) {
        Optional<Timetable> t = timetableRepository.getTimetableByGroup(group);
        if (t.isEmpty()) {
            throw new TimetableMissingException("Timetable for group " + group +
                    " does not exist!");
        }
        return t.get();
    }

    private List<Event> processRemoveRule(List<Rule> removeRules, Course course, Timetable timetable) {
        Rule rule = Rule.builder()
                .type("add")
                .id(UUID.randomUUID().toString())
                .course(course)
                .build();

        List<Event> events = getEventsFromAddRule(timetable, rule);

        return events.stream()
                .filter(event -> {
                    boolean checkEvent = true;

                    for (Rule value : removeRules) {
                        if (value.getId() != null &&
                                value.getCourse().getId().equals(course.getId()) &&
                                event.getStart().after(value.getCourse().getStartDate()) &&
                                event.getStart().before(value.getCourse().getEndDate())) {
                            checkEvent = false;
                            break;
                        }
                    }

                    return checkEvent;
                })
                .toList();
    }

    private List<Event> getEventsFromAddRule(Timetable timetable, Rule rule) {
        List<Event> events = new ArrayList<>();

        if (rule.getCourse().getFrequency().equalsIgnoreCase("once")) {
            Date startDate = addHour(rule.getCourse().getStartDate(), rule.getCourse().getStartHour());
            Date endDate = addHour(rule.getCourse().getStartDate(), rule.getCourse().getEndHour());

            Event event = Event.builder()
                    .title(rule.getCourse().getName() + " - " + StringUtils.capitalize(rule.getCourse().getType()))
                    .start(startDate)
                    .end(endDate)
                    .classroom(rule.getCourse().getClassroom())
                    .build();
            events.add(event);
        }

        if (rule.getCourse().getFrequency().equalsIgnoreCase("weekly")) {
            Date currentDate = rule.getCourse().getStartDate();

            while (currentDate.before(rule.getCourse().getEndDate()) ||
                    currentDate.equals(rule.getCourse().getEndDate())) {
                Event event = Event.builder()
                        .title(rule.getCourse().getName() + " - " + StringUtils.capitalize(rule.getCourse().getType()))
                        .start(addHour(currentDate, rule.getCourse().getStartHour()))
                        .end(addHour(currentDate, rule.getCourse().getEndHour()))
                        .classroom(rule.getCourse().getClassroom())
                        .build();
                events.add(event);

                currentDate = addDays(currentDate, 7);
            }
        }

        if (rule.getCourse().getFrequency().equalsIgnoreCase("even")) {
            Date currentDate = rule.getCourse().getStartDate();
            long weeksBetween = getWeeksBetween(currentDate, timetable.getStartDate());
            if ((weeksBetween + 1) % 2 != 0) {
                currentDate = addDays(currentDate, 7);
            }

            while (currentDate.before(rule.getCourse().getEndDate()) ||
                    currentDate.equals(rule.getCourse().getEndDate())) {
                Event event = Event.builder()
                        .title(rule.getCourse().getName() + " - " + StringUtils.capitalize(rule.getCourse().getType()))
                        .start(addHour(currentDate, rule.getCourse().getStartHour()))
                        .end(addHour(currentDate, rule.getCourse().getEndHour()))
                        .classroom(rule.getCourse().getClassroom())
                        .build();
                events.add(event);

                currentDate = addDays(currentDate, 14);
            }
        }

        if (rule.getCourse().getFrequency().equalsIgnoreCase("odd")) {
            Date currentDate = rule.getCourse().getStartDate();
            long weeksBetween = getWeeksBetween(currentDate, timetable.getStartDate());
            if ((weeksBetween + 1) % 2 == 0) {
                currentDate = addDays(currentDate, 7);
            }

            while (currentDate.before(rule.getCourse().getEndDate()) ||
                    currentDate.equals(rule.getCourse().getEndDate())) {
                Event event = Event.builder()
                        .title(rule.getCourse().getName() + " - " + StringUtils.capitalize(rule.getCourse().getType()))
                        .start(addHour(currentDate, rule.getCourse().getStartHour()))
                        .end(addHour(currentDate, rule.getCourse().getEndHour()))
                        .classroom(rule.getCourse().getClassroom())
                        .build();
                events.add(event);

                currentDate = addDays(currentDate, 14);
            }
        }

        return events;
    }

    public Date addDays(Date date, int days) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
        localDateTime = localDateTime.plusDays(days);

        return Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
    }

    public Date addHour(Date date, String hour) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
        String[] split = hour.split(":");
        localDateTime = localDateTime.plusHours(Integer.parseInt(split[0])).plusMinutes(Integer.parseInt(split[1]));

        return Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
    }

    public long getWeeksBetween(Date start, Date end) {
        LocalDateTime startLocalDate = start.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime endLocalDate = end.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();

        return ChronoUnit.WEEKS.between(startLocalDate, endLocalDate);
    }
}
