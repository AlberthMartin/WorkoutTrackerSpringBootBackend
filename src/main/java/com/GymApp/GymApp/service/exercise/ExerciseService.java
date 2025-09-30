package com.GymApp.GymApp.service.exercise;

import com.GymApp.GymApp.dto.ExerciseDto;
import com.GymApp.GymApp.exeptions.ActionNotAllowedException;
import com.GymApp.GymApp.exeptions.ResourceNotFoundException;
import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.User;
import com.GymApp.GymApp.repository.ExerciseRepository;
import com.GymApp.GymApp.repository.UserRepository;
import com.GymApp.GymApp.requests.exercise.AddExerciseRequest;
import com.GymApp.GymApp.requests.exercise.UpdateExerciseRequest;
import com.GymApp.GymApp.security.user.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService implements IExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    //Get all exercises, including the user specific ones for the current user, admins only get all the global ones
    @Override
    public List<Exercise> getAllExercises(AppUserDetails userDetails) {
        User currentUser = userRepository.findByEmail(userDetails.getUsername());
        return exerciseRepository.findByUserIdOrGlobal(currentUser.getId());
    }

    //Add exercise, user-specific and global exercises that the admin can create
    @Override
    public Exercise addExercise(AddExerciseRequest request, AppUserDetails userDetails, boolean isAdmin) {
        //Creating the exercise from the request using helper
        Exercise exercise = createExercise(request);

        if (isAdmin) {
            exercise.setCreatedBy(null); //Null for global exercises
        } else {
            User currentUser = userRepository.findByEmail(userDetails.getUsername());
            //User-specific exercise
            exercise.setCreatedBy(currentUser);
        }
        return exerciseRepository.save(exercise);
    }

    //Helper method that creates an exercise, used in addExercise method
    private Exercise createExercise(AddExerciseRequest request) {
        return new Exercise(
                request.getName(),
                request.getDescription(),
                request.getPrimaryMuscleGroup(),
                request.getSecondaryMuscleGroup(),
                request.getExerciseType()
        );
    }

    /*
    Get exercise by id, only the right user can se its exercises + global ones,
    admins can see all exercises
     */
    @Override
    public Exercise getExerciseById(Long exerciseId, AppUserDetails userDetails) {

        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        boolean isAdmin = currentUser.getRoles().contains("ROLE_ADMIN");

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        //Admins can see all exercises
        if (isAdmin) {
            return exercise;
        }
        //Global
        else if (exercise.getCreatedBy() == null) {
            return exercise;
        }
        //You created this exercise
        else if (exercise.getCreatedBy().getId().equals(currentUser.getId())) {
            return exercise;
        }
        //This exercise should not be seen by you :)
        else {
            return null;
        }
    }

    /*
    Users can delete exercises created by themselfes and not global exercises or exercises created by another user
     */
    @Override
    public void deleteExerciseByIdAsUser(Long exerciseId, AppUserDetails userDetails) {

        //Get current user
        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        //Get exercise
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        if (exercise.getCreatedBy() == null) {
            throw new ActionNotAllowedException("Global exercises cannot be deleted by users.");
        }

        //If the app user is the creator of the exercise, then delete
        if (exercise.getCreatedBy().getId().equals(currentUser.getId())) {
            exerciseRepository.delete(exercise);
        } else {
            throw new ActionNotAllowedException("You are not allowed to delete this exercise.");
        }

    }

    @Override
    public void deleteExerciseByIdAsAdmin(Long exerciseId) {
        //Only admins can reach this method from the controller
        exerciseRepository.findById(exerciseId)
                .ifPresentOrElse(exerciseRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException("Exercise not found");
                        });

    }

    @Override
    public Exercise updateExerciseAsAdmin(UpdateExerciseRequest request, Long exerciseId) {
        //Only admins can reach this method
        Exercise exercise = exerciseRepository.findById(exerciseId)
            .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        updateExistingExercise(exercise, request);
        return exerciseRepository.save(exercise);
    }

    @Override
    public Exercise updateExerciseAsUser(UpdateExerciseRequest request, Long exerciseId, AppUserDetails userDetails) {
        //The current user
        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        // Find the exercise to be edited
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

        //If the exercise is NOT created by the user, then the user can NOT update
        if (!exercise.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ActionNotAllowedException("You are not allowed to edit this exercise.");
        }
        //this exercise was created by user current user
        updateExistingExercise(exercise, request);
        return exerciseRepository.save(exercise);
    }

    private void updateExistingExercise(Exercise exercise, UpdateExerciseRequest request) {
        exercise.setName(request.getName());
        exercise.setDescription(request.getDescription());
        exercise.setPrimaryMuscleGroup(request.getPrimaryMuscleGroup());
        exercise.setSecondaryMuscleGroup(request.getSecondaryMuscleGroup());
        exercise.setExerciseType(request.getExerciseType());
    }

    @Override
    public List<ExerciseDto> getConvertedExercises(List<Exercise> exercises) {
        return exercises.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ExerciseDto convertToDto(Exercise exercise) {
        return modelMapper.map(exercise, ExerciseDto.class);
    }


}
