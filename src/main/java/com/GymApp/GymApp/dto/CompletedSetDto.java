package com.GymApp.GymApp.dto;

import lombok.Data;

@Data
public class CompletedSetDto {
    private Long id;
    private int reps;
    private double weight;
    private Long exerciseId;
    private String exerciseName;
}
