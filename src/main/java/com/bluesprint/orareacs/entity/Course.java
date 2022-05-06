package com.bluesprint.orareacs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
@Builder
public class Course {
    private String id;
    private String name;
    private String type;
    private Date startDate;
    private Date endDate;
    private String frequency;
    private String startHour;
    private String endHour;
}
