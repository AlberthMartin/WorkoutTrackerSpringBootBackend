package com.GymApp.GymApp.controller;

import com.GymApp.GymApp.dto.WorkoutTemplateDto;
import com.GymApp.GymApp.exeptions.ActionNotAllowedException;
import com.GymApp.GymApp.exeptions.ResourceNotFoundException;
import com.GymApp.GymApp.model.WorkoutTemplate;
import com.GymApp.GymApp.requests.workout.CreateWorkoutTemplateRequest;
import com.GymApp.GymApp.response.ApiResponse;
import com.GymApp.GymApp.security.user.AppUserDetails;
import com.GymApp.GymApp.service.exercise.IExerciseService;
import com.GymApp.GymApp.service.workout.IWorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/workouts")
public class WorkoutController {

    private final IWorkoutService workoutService;
    private final IExerciseService exerciseService;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> allWorkouts(@AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            List<WorkoutTemplate> workoutTemplates = workoutService.getAllWorkoutTemplates(userDetails);
            List<WorkoutTemplateDto> workoutTemplateDtos = workoutService.getConvertedWorkoutTemplates(workoutTemplates);

            return ResponseEntity.ok(new ApiResponse("success, workouts fetched ", workoutTemplateDtos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')" )
    @PostMapping("/add/user/workout")
    public ResponseEntity<ApiResponse> addUserWorkoutTemplate(@RequestBody CreateWorkoutTemplateRequest request, @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            WorkoutTemplate workoutTemplate = workoutService.addUserWorkoutTemplate(request, userDetails);
            WorkoutTemplateDto workoutTemplateDto = workoutService.convertToDto(workoutTemplate);
            //Convert the reference exercise to dto IDK if this is necessary
            workoutTemplateDto.getWorkoutExercises().stream().map(ex-> exerciseService.convertToDto(ex.getExercise()));

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("New workout created successfully", workoutTemplateDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/workout/{id}/user/update")
    public ResponseEntity<ApiResponse> updateUserWorkoutTemplate(@PathVariable Long id, @RequestBody CreateWorkoutTemplateRequest request, @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            WorkoutTemplate workoutTemplate = workoutService.updateUserWorkoutTemplate(request,userDetails,id);
            WorkoutTemplateDto workoutTemplateDto = workoutService.convertToDto(workoutTemplate);
            //Convert the reference exercise to dto IDK if this is necessary
            workoutTemplateDto.getWorkoutExercises().stream().map(ex-> exerciseService.convertToDto(ex.getExercise()));

            return ResponseEntity.ok(new ApiResponse("Workout template updated successfully", workoutTemplateDto));

        } catch (ActionNotAllowedException | ResourceNotFoundException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }

    }


    //@DeleteMapping("/exercise/{id}/user/delete")
}
