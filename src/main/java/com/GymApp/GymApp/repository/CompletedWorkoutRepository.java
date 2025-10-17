package com.GymApp.GymApp.repository;

import com.GymApp.GymApp.model.CompletedWorkout;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CompletedWorkoutRepository extends CrudRepository<CompletedWorkout, Integer> {

    //Gett all users completed workouts
    List<CompletedWorkout> findByUserId(Long userId);

    CompletedWorkout findByWorkoutId(Long workoutId);
}
