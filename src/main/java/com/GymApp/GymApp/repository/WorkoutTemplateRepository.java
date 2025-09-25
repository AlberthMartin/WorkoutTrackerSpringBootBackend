package com.GymApp.GymApp.repository;

import com.GymApp.GymApp.model.WorkoutTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplate, Long> {

     List<WorkoutTemplate> findByCreatedById(Long userId);
}
