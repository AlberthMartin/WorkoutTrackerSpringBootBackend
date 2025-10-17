package com.GymApp.GymApp.requests.history;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SaveCompletedWorkoutRequest {
    private LocalDate completedAt;
    private Long durationSeconds;
    private String notes;
    private List<SaveCompletedSetRequest> sets;
}
