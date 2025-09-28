package com.GymApp.GymApp.requests.exercise;

import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import com.GymApp.GymApp.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateExerciseRequest {
    private Long id;
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private MuscleGroup primaryMuscleGroup;
    private MuscleGroup secondaryMuscleGroup;
    private ExerciseType exerciseType;
}
