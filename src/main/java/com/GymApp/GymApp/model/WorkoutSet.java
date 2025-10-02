package com.GymApp.GymApp.model;

import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.WorkoutExercise;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkoutSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_exercise_id")
    private WorkoutExercise workoutExercise;

    private int reps;
    private double weight;
    private boolean completed;

    public WorkoutSet(int reps, double weight) {
        this.reps = reps;
        this.weight = weight;
        this.completed = false;
    }
}