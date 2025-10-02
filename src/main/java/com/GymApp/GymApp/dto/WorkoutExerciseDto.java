package com.GymApp.GymApp.dto;

import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.WorkoutExercise;
import com.GymApp.GymApp.model.WorkoutSet;
import com.GymApp.GymApp.model.WorkoutTemplate;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutExerciseDto {
    private int OrderNumber;
    private int restSeconds;
    private Exercise exercise;
    private List<WorkoutSetDto> workoutSets;
}
