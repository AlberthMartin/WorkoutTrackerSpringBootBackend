package com.GymApp.GymApp.requests.history;

import com.GymApp.GymApp.model.CompletedWorkout;
import com.GymApp.GymApp.model.Exercise;

import lombok.Data;

@Data
public class UpdateCompletedSetRequest {
    private Long setId; //notice set id when updating but not when creating
    private Long exerciseId;
    private int orderNumber;
    private int restSeconds;
    private int reps;
    private int weight;
}
