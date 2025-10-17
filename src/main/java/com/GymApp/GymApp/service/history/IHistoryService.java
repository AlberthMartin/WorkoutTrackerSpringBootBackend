package com.GymApp.GymApp.service.history;

import com.GymApp.GymApp.dto.CompletedWorkoutDto;
import com.GymApp.GymApp.model.CompletedWorkout;
import com.GymApp.GymApp.model.WorkoutTemplate;
import com.GymApp.GymApp.requests.history.SaveCompletedWorkoutRequest;
import com.GymApp.GymApp.requests.history.UpdateCompletedWorkoutRequest;
import com.GymApp.GymApp.security.user.AppUserDetails;

import java.util.List;

public interface IHistoryService {

    List<CompletedWorkoutDto> getCompletedWorkouts(AppUserDetails userDetails);

    CompletedWorkoutDto saveCompletedWorkout(SaveCompletedWorkoutRequest request, AppUserDetails userDetails);

    void deleteCompletedWorkout(AppUserDetails userDetails,  Long completedWorkoutId);

    CompletedWorkoutDto updateCompletedWorkout(UpdateCompletedWorkoutRequest request, AppUserDetails userDetails, Long completedWorkoutId);

}
