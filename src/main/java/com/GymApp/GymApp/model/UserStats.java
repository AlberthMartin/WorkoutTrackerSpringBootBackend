package com.GymApp.GymApp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class UserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int totalWorkouts;
    private int totalDurationSeconds; //all workouts
    private int totalSets; //all exercises
    private int totalReps; //all exercises

    private LocalDate date;
}
