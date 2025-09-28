package com.GymApp.GymApp.service.exercise;

import com.GymApp.GymApp.dto.ExerciseDto;
import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.User;
import com.GymApp.GymApp.requests.exercise.AddExerciseRequest;
import com.GymApp.GymApp.requests.exercise.UpdateExerciseRequest;
import com.GymApp.GymApp.security.user.AppUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface IExerciseService {
    //Get all exercises including the user specifik ones for the current user
    List<Exercise> getAllExercises(AppUserDetails userDetails);

    //Add exercise, user-specific and global exercises that the admin can create
    Exercise addExercise(AddExerciseRequest request, AppUserDetails userDetails, boolean isAdmin);

    Exercise getExerciseById(Long exerciseId, AppUserDetails userDetails);

    void deleteExerciseByIdAsUser(Long exerciseId, AppUserDetails userDetails);

    void deleteExerciseByIdAsAdmin(Long exerciseId, AppUserDetails userDetails);

    Exercise updateExerciseAsAdmin(UpdateExerciseRequest request, Long exerciseId, AppUserDetails userDetails);

    Exercise updateExerciseAsUser(UpdateExerciseRequest request, Long exerciseId, AppUserDetails userDetails);

    List<ExerciseDto> getConvertedExercises(List<Exercise> exercises);

    ExerciseDto convertToDto(Exercise exercise);
}
