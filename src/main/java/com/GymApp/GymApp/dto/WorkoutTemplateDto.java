package com.GymApp.GymApp.dto;

import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.WorkoutExercise;
import lombok.Data;

import java.util.List;

@Data
public class WorkoutTemplateDto {
    private Long id;
    private String name;
    private String description;
    private List<WorkoutExerciseDto> workoutExercises;
}
