package com.GymApp.GymApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

//This is used to show PRs and volume trends per exercise
@Entity
@Data
public class ExerciseStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    private double bestSet; //Est 1RM
    private double maxWeight; //most weight lifted
    private double maxReps; //max reps in one set

    private double totalVolume; //volume for all time

    private int totalSets;
    private int totalReps;

    private LocalDate lastUpdated;
}
