package com.GymApp.GymApp.dto;

import lombok.Data;

@Data
public class WorkoutSetDto {
    private int reps;
    private double weight;
    private boolean completed;
}
