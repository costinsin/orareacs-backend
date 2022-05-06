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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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
    public void deleteTimetableByGroup(String group) {
        if (!timetableRepository.existsByGroup(group)) {
            throw new TimetableMissingException("Timetable for group: " + group +
                    " does not exist!");
        }

        timetableRepository.deleteByGroup(group);
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
                .collect(Collectors.toList());
        List<Rule> removeRules = user.getRules().stream()
                .filter(rule -> rule.getType().equalsIgnoreCase("remove"))
                .collect(Collectors.toList());

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
                        if (value.getCourse().getId().equals(course.getId()) &&
                                event.getStartDate().after(value.getCourse().getStartDate()) &&
                                event.getStartDate().before(value.getCourse().getEndDate())) {
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
                    .name(rule.getCourse().getName() + " - " + StringUtils.capitalize(rule.getCourse().getType()))
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
            events.add(event);
        }

        if (rule.getCourse().getFrequency().equalsIgnoreCase("weekly")) {
            Date currentDate = rule.getCourse().getStartDate();

            while (currentDate.before(rule.getCourse().getEndDate()) ||
                    currentDate.equals(rule.getCourse().getEndDate())) {
                Event event = Event.builder()
                        .name(rule.getCourse().getName() + " - " + StringUtils.capitalize(rule.getCourse().getType()))
                        .startDate(addHour(currentDate, rule.getCourse().getStartHour()))
                        .endDate(addHour(currentDate, rule.getCourse().getEndHour()))
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
                        .name(rule.getCourse().getName() + " - " + StringUtils.capitalize(rule.getCourse().getType()))
                        .startDate(addHour(currentDate, rule.getCourse().getStartHour()))
                        .endDate(addHour(currentDate, rule.getCourse().getEndHour()))
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
                        .name(rule.getCourse().getName() + " - " + StringUtils.capitalize(rule.getCourse().getType()))
                        .startDate(addHour(currentDate, rule.getCourse().getStartHour()))
                        .endDate(addHour(currentDate, rule.getCourse().getEndHour()))
                        .build();
                events.add(event);

                currentDate = addDays(currentDate, 14);
            }
        }

        return events;
    }

    public Date addDays(Date date, int days) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusDays(days);

        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date addHour(Date date, String hour) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        String[] split = hour.split(":");
        localDateTime = localDateTime.plusHours(Integer.parseInt(split[0])).plusMinutes(Integer.parseInt(split[1]));
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public long getWeeksBetween(Date start, Date end) {
        LocalDateTime startLocalDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endLocalDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return ChronoUnit.WEEKS.between(startLocalDate, endLocalDate);
    }
}
