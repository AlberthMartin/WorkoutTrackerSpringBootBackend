package com.GymApp.GymApp.requests.history;

import com.GymApp.GymApp.model.CompletedWorkout;
import com.GymApp.GymApp.model.Exercise;
import lombok.Data;

@Data
public class SaveCompletedSetRequest {
    private Long exerciseId;
    private int orderNumber;
    private int restSeconds;
    private int reps;
    private int weight;
}
