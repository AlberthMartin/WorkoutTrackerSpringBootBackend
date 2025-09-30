package com.GymApp.GymApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int orderNumber;   // exercise position in workout
    private int restSeconds;   // typical rest between sets

    @ManyToOne
    @JoinColumn(name = "workout_template_id", nullable = false)
    private WorkoutTemplate workoutTemplate;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;
    //KSK kan ha denhä som Long exerciseId


    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutSet> sets = new ArrayList<>();
}

