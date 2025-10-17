package com.GymApp.GymApp.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CompletedWorkoutDto {
    private Long id;
    private Long userId;
    private Long workoutId;
    private LocalDate completedAt;
    private Long durationSeconds;
    private String notes;
    private List<CompletedSetDto> sets;
}
