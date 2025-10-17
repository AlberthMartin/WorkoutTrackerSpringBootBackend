package com.GymApp.GymApp.model;

import com.GymApp.GymApp.dto.WorkoutSetDto;
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

    public void addSet(WorkoutSet set){
        set.setWorkoutExercise(this); //connect set to this workoutExercise
        this.sets.add(set); //ass the set to this exercise
    }

    public void removeSet(WorkoutSet set){
        this.sets.remove(set); //remove set
        set.setWorkoutExercise(null); //disconnect
    }

    public void updateSet(WorkoutSet set, int reps, double weight){
        set.setWeight(weight);
        set.setReps(reps);
    }

     public int totalReps() {
        return sets.stream().mapToInt(WorkoutSet::getReps).sum();
    }

    public double totalVolume() {
        return sets.stream().mapToDouble(s -> s.getReps() * s.getWeight()).sum();
    }

    //TODO: In completed Workouts: Calculate volume per muscle group, sets per muscle group, total volume
}

