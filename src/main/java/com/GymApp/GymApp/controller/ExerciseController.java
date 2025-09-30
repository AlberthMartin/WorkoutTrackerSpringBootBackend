package com.GymApp.GymApp.controller;

import com.GymApp.GymApp.dto.ExerciseDto;
import com.GymApp.GymApp.exeptions.ActionNotAllowedException;
import com.GymApp.GymApp.exeptions.ResourceNotFoundException;
import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.User;
import com.GymApp.GymApp.requests.exercise.AddExerciseRequest;
import com.GymApp.GymApp.requests.exercise.UpdateExerciseRequest;
import com.GymApp.GymApp.response.ApiResponse;
import com.GymApp.GymApp.security.user.AppUserDetails;
import com.GymApp.GymApp.service.exercise.IExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

/*
Get all exercises	GET	/exercises/all	@AuthenticationPrincipal userDetails
Get exercise by ID	GET	/exercise/{id}/exercise	@PathVariable id, @AuthenticationPrincipal userDetails

Add user Exercise	POST	/add/user/exercise	@RequestBody AddExerciseRequest, @AuthenticationPrincipal userDetails
Add Global Exercise	POST	/add/global/exercise	@RequestBody  AddExerciseRequest, @AuthenticationPrincipal userDetails

Delete exercise as USER	DELETE	/exercise/{id}/user/delete	@PathVariable id, @AuthenticationPrincipal userDetails
Delete exercise as ADMIN	DELETE	/exercise/{id}/admin/delete	@PathVariable id, @AuthenticationPrincipal userDetails

UPDATE exercise as USER	PUT	/exercises/exercise2/user/update	@PathVariable id, @AuthenticationPrincipal userDetails, @RequestBody  UpdateExerciseRequest
UPDATE exercise as ADMIN	PUT	/exercises/exercise2/admin/update	@PathVariable id,  @RequestBody  UpdateExerciseRequest

*/

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/exercises")
public class ExerciseController {

    private final IExerciseService exerciseService;

    //Get all global exercises and the user specific ones
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllExercises(@AuthenticationPrincipal AppUserDetails userDetails) {
        try {
            List<Exercise> exercises = exerciseService.getAllExercises(userDetails);
            List<ExerciseDto> convertedExercises = exerciseService.getConvertedExercises(exercises);
            return ResponseEntity.ok(new ApiResponse("success, exercises fetched", convertedExercises));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    //Get exercise by id
    @GetMapping("/exercise/{id}/exercise")
    public ResponseEntity<ApiResponse> getExerciseById(@PathVariable Long id, @AuthenticationPrincipal AppUserDetails userDetails){
        try {
            Exercise exercise = exerciseService.getExerciseById(id, userDetails);

            if(exercise == null){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("You can't see this exercise or it does not exist", null));
            }

            ExerciseDto exerciseDto = exerciseService.convertToDto(exercise);
            return ResponseEntity.ok(new ApiResponse("success, exercise fetched", exerciseDto));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    //RequestBody = JSON with the fields from the frontend, then gets currently autenticatd user
    @PostMapping("/add/user/exercise")
    @PreAuthorize("hasRole('ROLE_USER')" )
    public ResponseEntity<ApiResponse> addUserExercise(@RequestBody AddExerciseRequest request, @AuthenticationPrincipal AppUserDetails userDetails){
        try {
            Exercise exercise = exerciseService.addExercise(request, userDetails, false);
            ExerciseDto exerciseDto = exerciseService.convertToDto(exercise);
            return ResponseEntity.ok(new ApiResponse("success, user exercise added", exerciseDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    //Admins can add global exercises
    @PostMapping("/add/global/exercise")
    @PreAuthorize("hasRole('ROLE_ADMIN')" )
    public ResponseEntity<ApiResponse> addGlobalExercise(@RequestBody AddExerciseRequest request, @AuthenticationPrincipal AppUserDetails userDetails){
        try {
            Exercise exercise = exerciseService.addExercise(request, userDetails, true);
            ExerciseDto exerciseDto = exerciseService.convertToDto(exercise);
            return ResponseEntity.ok(new ApiResponse("success, global exercise added", exerciseDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/exercise/{id}/admin/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')" )
    public ResponseEntity<ApiResponse> deleteExerciseByIdAsAdmin(@PathVariable Long id, @AuthenticationPrincipal AppUserDetails userDetails){
        try{
            exerciseService.deleteExerciseByIdAsAdmin(id);
            return ResponseEntity.ok(new ApiResponse("success, exercise deleted as Admin", true));
        }catch (ResourceNotFoundException | ActionNotAllowedException e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/exercise/{id}/user/delete")
    @PreAuthorize("hasRole('ROLE_USER')" )
    public ResponseEntity<ApiResponse> deleteExerciseByIdAsUser(@PathVariable Long id, @AuthenticationPrincipal AppUserDetails userDetails){
        try{
            exerciseService.deleteExerciseByIdAsUser(id, userDetails);
            return ResponseEntity.ok(new ApiResponse("success, exercise deleted as User", null));
        }catch (ResourceNotFoundException | ActionNotAllowedException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/exercise/{id}/user/update")
    @PreAuthorize("hasRole('ROLE_USER')" )
    public ResponseEntity<ApiResponse> updateExerciseAsUser(@PathVariable Long id, @RequestBody UpdateExerciseRequest request, @AuthenticationPrincipal AppUserDetails userDetails){
        try{
            Exercise exercise = exerciseService.updateExerciseAsUser(request, id, userDetails);
            ExerciseDto exerciseDto = exerciseService.convertToDto(exercise);
            return ResponseEntity.ok(new ApiResponse("success, exercise updated as USER", exerciseDto));
        }catch (ResourceNotFoundException | ActionNotAllowedException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/exercise/{id}/admin/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')" )
    public ResponseEntity<ApiResponse> updateExerciseAsAdmin(@PathVariable Long id, @RequestBody UpdateExerciseRequest request){
        try{
            Exercise exercise = exerciseService.updateExerciseAsAdmin(request, id);
            ExerciseDto exerciseDto = exerciseService.convertToDto(exercise);
            return ResponseEntity.ok(new ApiResponse("success, exercise updated as ADMIN", exerciseDto));
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


}
