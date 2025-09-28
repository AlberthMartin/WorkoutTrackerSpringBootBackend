package com.GymApp.GymApp.dto;

import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import com.GymApp.GymApp.model.User;
import lombok.Data;

@Data
public class ExerciseDto {
    private Long id;
    private String name;
    private String description;
    private UserDto createdBy;
    private MuscleGroup primaryMuscleGroup;
    private MuscleGroup secondaryMuscleGroup;
    private ExerciseType exerciseType;
}
