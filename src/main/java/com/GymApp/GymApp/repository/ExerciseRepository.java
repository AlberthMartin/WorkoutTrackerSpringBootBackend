package com.GymApp.GymApp.repository;

import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import com.GymApp.GymApp.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByName(String name);

    List<Exercise> findByPrimaryMuscleGroup(MuscleGroup muscleGroup);

    List<Exercise> findByExerciseType(ExerciseType exerciseType);


            /*
             List<Exercise> getExercisesByPrimaryMuscleGroup(MuscleGroup muscleGroup);
    List<Exercise> getExercisesByExerciseType(ExerciseType exerciseType);
    List<Exercise> getExercisesByName(String name);
             */

}
