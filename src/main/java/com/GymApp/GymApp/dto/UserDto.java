package com.GymApp.GymApp.dto;

import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.WorkoutTemplate;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private List<WorkoutTemplate> workoutTemplates;
    private List<Exercise> exercises;
    //private List<CompletedWorkout> completedWorkouts
}
