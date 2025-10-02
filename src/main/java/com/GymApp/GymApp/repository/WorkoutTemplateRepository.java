package com.GymApp.GymApp.repository;

import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.WorkoutTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplate, Long> {

     List<WorkoutTemplate> findByCreatedById(Long userId);

     @Query("SELECT e FROM WorkoutTemplate e WHERE e.createdBy.id = :userId OR e.createdBy IS NULL")
    List<WorkoutTemplate> findByUserIdOrGlobal(@Param("userId") Long userId);

}
