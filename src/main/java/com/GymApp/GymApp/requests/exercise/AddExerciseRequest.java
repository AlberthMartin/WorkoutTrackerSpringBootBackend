package com.GymApp.GymApp.requests.exercise;

import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import com.GymApp.GymApp.model.User;
import lombok.Data;

@Data
public class AddExerciseRequest {
    private Long id;
    private User createdBy;
    private String name;
    private String description;
    private MuscleGroup primaryMuscleGroup;
    private MuscleGroup secondaryMuscleGroup;
    private ExerciseType exerciseType;

}
