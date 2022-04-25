package com.bluesprint.orareacs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Course {
    private String name;
    private String type;
    private String startDate;
    private String endDate;
    private String frequency;
    private String startHour;
    private String endHour;
}
