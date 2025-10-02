package com.GymApp.GymApp.service.workout;

import com.GymApp.GymApp.dto.WorkoutTemplateDto;
import com.GymApp.GymApp.model.WorkoutTemplate;
import com.GymApp.GymApp.requests.workout.CreateWorkoutTemplateRequest;
import com.GymApp.GymApp.security.user.AppUserDetails;

import java.util.List;

public interface IWorkoutService {

    List<WorkoutTemplate> getAllWorkoutTemplates(AppUserDetails userDetails);

    WorkoutTemplate addUserWorkoutTemplate(CreateWorkoutTemplateRequest request, AppUserDetails userDetails);

    List<WorkoutTemplateDto> getConvertedWorkoutTemplates(List<WorkoutTemplate> workoutTemplates);

    WorkoutTemplateDto convertToDto(WorkoutTemplate template);
}
