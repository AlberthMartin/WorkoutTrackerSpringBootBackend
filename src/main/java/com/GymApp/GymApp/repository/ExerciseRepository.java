package com.GymApp.GymApp.repository;

import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import com.GymApp.GymApp.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {


    @Query("SELECT e FROM Exercise e WHERE e.createdBy.id = :userId OR e.createdBy IS NULL")
    List<Exercise> findByUserIdOrGlobal(@Param("userId") Long userId);


}
