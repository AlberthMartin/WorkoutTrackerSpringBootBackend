package com.GymApp.GymApp.controller;

import com.GymApp.GymApp.dto.CompletedWorkoutDto;
import com.GymApp.GymApp.exeptions.ActionNotAllowedException;
import com.GymApp.GymApp.exeptions.ResourceNotFoundException;
import com.GymApp.GymApp.model.CompletedWorkout;
import com.GymApp.GymApp.requests.history.SaveCompletedWorkoutRequest;
import com.GymApp.GymApp.requests.history.UpdateCompletedWorkoutRequest;
import com.GymApp.GymApp.requests.workout.CreateWorkoutTemplateRequest;
import com.GymApp.GymApp.response.ApiResponse;
import com.GymApp.GymApp.security.user.AppUserDetails;
import com.GymApp.GymApp.service.history.IHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/history")
public class HistoryController {

    private final IHistoryService historyService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all/user/completed-workouts")
    public ResponseEntity<ApiResponse> getUserCompletedWorkout( @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            List<CompletedWorkoutDto> completedWorkoutDtos = historyService.getCompletedWorkouts(userDetails);
            return ResponseEntity.ok(new ApiResponse("completed workouts fetched successfully", completedWorkoutDtos));
        }catch (ResourceNotFoundException e){}
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("no completed workouts found or something whent wrong idk", null));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/save/user/completed-workout")
    public ResponseEntity<ApiResponse> saveUserCompletedWorkout(@RequestBody SaveCompletedWorkoutRequest request, @AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            CompletedWorkoutDto savedCompletedWorkout = historyService.saveCompletedWorkout(request, userDetails);

            return ResponseEntity.ok(new ApiResponse("completed workout saved successfully", savedCompletedWorkout));

        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("exercise or user not found when saving a completed workout", HttpStatus.NOT_FOUND));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete/user/completed-workout/{completedWorkoutId}")
    public ResponseEntity<ApiResponse> deleteCompletedWorkout(@AuthenticationPrincipal AppUserDetails userDetails, @PathVariable Long completedWorkoutId) {
        try {
            historyService.deleteCompletedWorkout(userDetails, completedWorkoutId);
            return ResponseEntity.ok(new ApiResponse("completed workout deleted successfully", completedWorkoutId));
        }catch (ResourceNotFoundException | ActionNotAllowedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("not authorized", null));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/update/user/completed-workout/{completedWorkoutId}")
    public ResponseEntity<ApiResponse> updateCompletedWorkout(@AuthenticationPrincipal AppUserDetails userDetails, @PathVariable Long completedWorkoutId, @RequestBody UpdateCompletedWorkoutRequest request) {
        try {
            CompletedWorkoutDto updatedWorkoutDto = historyService.updateCompletedWorkout(request, userDetails, completedWorkoutId);
            return ResponseEntity.ok(new ApiResponse("completed workout updated successfully", updatedWorkoutDto));
        }catch (ResourceNotFoundException | ActionNotAllowedException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("not authorized", null));
        }
    }


}
