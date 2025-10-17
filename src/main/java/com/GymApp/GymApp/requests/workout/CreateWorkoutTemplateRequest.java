package com.GymApp.GymApp.requests.workout;

import com.GymApp.GymApp.model.User;
import com.GymApp.GymApp.model.WorkoutExercise;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateWorkoutTemplateRequest {
    private Long id;
    @NotBlank
    private String name;
    private String description;
    private List<CreateWorkoutExerciseRequest> workoutExercises;

}
