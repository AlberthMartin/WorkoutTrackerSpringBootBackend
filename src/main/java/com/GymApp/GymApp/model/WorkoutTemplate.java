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
public class WorkoutTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = true, updatable = false)
    private User createdBy;

    //cascade = CascadeType.ALL --> when you save/delete a workout, its
    //WorkoutExercise entities are saved/deleted automatically
    //orphanRemoval = true → removes WorkoutExercise entries if they are removed from the list
    //One workout contains many workoutExercises
    @OneToMany(mappedBy = "workoutTemplate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> WorkoutExercises = new ArrayList<>();

    public void addExercise(WorkoutExercise exercise) {
        this.WorkoutExercises.add(exercise); //Add the exercis
        exercise.setWorkoutTemplate(this); //Connect the exercise to this template
    }

    public void removeExercise(WorkoutExercise exercise) {
        WorkoutExercises.remove(exercise);
        exercise.setWorkoutTemplate(null);
    }


}
