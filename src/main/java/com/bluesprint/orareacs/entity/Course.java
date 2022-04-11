package com.bluesprint.orareacs.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Course {
    private String name;
    private String day;
    private String startHour;
    private String endHour;
    private String courseType;
    private int frequency;
}
