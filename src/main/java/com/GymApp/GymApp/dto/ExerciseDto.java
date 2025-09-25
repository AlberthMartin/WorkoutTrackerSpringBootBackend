package com.GymApp.GymApp.dto;

import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import com.GymApp.GymApp.model.User;

public class ExerciseDto {
    private Long id;
    private String name;
    private String description;
    private User createdBy;
    private MuscleGroup primaryMuscleGroup;
    private MuscleGroup secondaryMuscleGroup;
    private ExerciseType exerciseType;
}
