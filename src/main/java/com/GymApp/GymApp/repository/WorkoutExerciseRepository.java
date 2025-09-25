package com.GymApp.GymApp.repository;

import com.GymApp.GymApp.model.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
}
