package com.GymApp.GymApp.dto;

import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.WorkoutExercise;
import com.GymApp.GymApp.model.WorkoutSet;
import com.GymApp.GymApp.model.WorkoutTemplate;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutExerciseDto {
    private Long id;
    private int OrderNumber;
    private int restSeconds;
    private ExerciseDto exerciseDto;
    private List<WorkoutSetDto> workoutSets;
}
