package com.GymApp.GymApp.model;

import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.WorkoutExercise;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class WorkoutSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_exercise_id", nullable = false)
    private WorkoutExercise workoutExercise;

    private int reps;
    private double weight;
    private boolean completed;
}