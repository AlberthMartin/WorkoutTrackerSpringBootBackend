package com.GymApp.GymApp.requests.workout;

import lombok.Data;

@Data
public class CreateWorkoutSetRequest {
    private int reps;
    private double weight;
}
