package com.bluesprint.orareacs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
@AllArgsConstructor
@Data
@Builder
public class Timetable {
    @Id
    private String id;
    @Indexed(unique = true)
    private String group;
    private Date startDate;
    private Date endDate;
    private List<Course> courses;
}
