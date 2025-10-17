package com.GymApp.GymApp.model;

import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = true)
    private User createdBy;

    @Enumerated(EnumType.STRING)
    private MuscleGroup primaryMuscleGroup;

    @Enumerated(EnumType.STRING)
    private MuscleGroup secondaryMuscleGroup;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    //List of all the workouts the exercise is in
    //One exercise can belong to many workoutExercises
    @JsonIgnore
    @OneToMany(mappedBy = "exercise")
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();

    public Exercise(String name, String description, MuscleGroup primaryMuscleGroup, MuscleGroup secondaryMuscleGroup, ExerciseType exerciseType, User user) {
        this.name = name;
        this.description = description;
        this.primaryMuscleGroup = primaryMuscleGroup;
        this.secondaryMuscleGroup = secondaryMuscleGroup;
        this.exerciseType = exerciseType;
        this.createdBy = user;
    }

    public Exercise(@NotBlank String name, String description, @NotBlank MuscleGroup primaryMuscleGroup, MuscleGroup secondaryMuscleGroup, ExerciseType exerciseType) {
        this.name = name;
        this.description = description;
        this.primaryMuscleGroup = primaryMuscleGroup;
        this.secondaryMuscleGroup = secondaryMuscleGroup;
        this.exerciseType = exerciseType;
        this.createdBy = null;
    }

}
