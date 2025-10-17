package com.GymApp.GymApp.requests.history;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateCompletedWorkoutRequest {
    private Long id;
    private LocalDate completedAt;
    private Long durationSeconds;
    private String notes;
    private List<UpdateCompletedSetRequest> sets;

}
