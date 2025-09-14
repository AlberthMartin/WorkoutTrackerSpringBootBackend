package com.GymApp.GymApp.model;

import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private MuscleGroup primaryMuscleGroup;

    @Enumerated(EnumType.STRING)
    private MuscleGroup secondaryMuscleGroup;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    //List of all the workouts the exercise is in
    //One exercise can belong to many workoutExercises
    @OneToMany(mappedBy = "exercise")
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();

    public Exercise(String name, String description, MuscleGroup primaryMuscleGroup, MuscleGroup secondaryMuscleGroup, ExerciseType exerciseType) {
           this.name = name;
           this.description = description;
           this.primaryMuscleGroup = primaryMuscleGroup;
           this.secondaryMuscleGroup = secondaryMuscleGroup;
           this.exerciseType = exerciseType;
    }

    public Exercise() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MuscleGroup getPrimaryMuscleGroup() {
        return primaryMuscleGroup;
    }

    public void setPrimaryMuscleGroup(MuscleGroup primaryMuscleGroup) {
        this.primaryMuscleGroup = primaryMuscleGroup;
    }

    public MuscleGroup getSecondaryMuscleGroup() {
        return secondaryMuscleGroup;
    }

    public void setSecondaryMuscleGroup(MuscleGroup secondaryMuscleGroup) {
        this.secondaryMuscleGroup = secondaryMuscleGroup;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public List<WorkoutExercise> getWorkoutExercises() {
        return workoutExercises;
    }

    public void setWorkoutExercises(List<WorkoutExercise> workoutExercises) {
        this.workoutExercises = workoutExercises;
    }
}
