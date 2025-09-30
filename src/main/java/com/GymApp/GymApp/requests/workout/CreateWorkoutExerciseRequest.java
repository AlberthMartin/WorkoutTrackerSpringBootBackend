package com.GymApp.GymApp.requests.workout;

import com.GymApp.GymApp.model.Exercise;
import lombok.Data;

import java.util.List;

@Data
public class CreateWorkoutExerciseRequest {
    private Long exerciseId;
    private int orderNumber;
    private int restSeconds;
    private List<CreateWorkoutSetRequest> workoutSets;
}

