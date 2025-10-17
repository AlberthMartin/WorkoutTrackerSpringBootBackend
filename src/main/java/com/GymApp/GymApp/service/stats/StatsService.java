package com.GymApp.GymApp.service.stats;

import com.GymApp.GymApp.repository.CompletedWorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final CompletedWorkoutRepository completedWorkoutRepository;
    private final UserStatsPeriodRepository userStatsPeriodRepository;
    private final ExerciseStatsRepository exerciseStatsRepository;
}
