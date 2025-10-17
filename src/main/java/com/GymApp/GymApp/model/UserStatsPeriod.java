package com.GymApp.GymApp.model;

//Keeps aggregate stats for each period (week month...)

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class UserStatsPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;

    private int totalSets;
    private int totalReps;
    private double totalVolume;
    private int totalWorkouts;
    private int totalDurationSeconds;
}
