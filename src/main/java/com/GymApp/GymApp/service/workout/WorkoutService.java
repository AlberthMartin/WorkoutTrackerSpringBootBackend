package com.GymApp.GymApp.service.workout;


import com.GymApp.GymApp.dto.WorkoutExerciseDto;
import com.GymApp.GymApp.dto.WorkoutSetDto;
import com.GymApp.GymApp.dto.WorkoutTemplateDto;
import com.GymApp.GymApp.exeptions.ActionNotAllowedException;
import com.GymApp.GymApp.exeptions.ResourceNotFoundException;
import com.GymApp.GymApp.model.*;
import com.GymApp.GymApp.repository.ExerciseRepository;
import com.GymApp.GymApp.repository.UserRepository;
import com.GymApp.GymApp.repository.WorkoutTemplateRepository;
import com.GymApp.GymApp.requests.workout.CreateWorkoutTemplateRequest;
import com.GymApp.GymApp.security.user.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
This is something we want from the frontend
{
  "name": "Push Day Strength",
  "description": "Chest, shoulders, and triceps focused workout.",
  "workoutExercises": [
    {
      "exerciseId": 1,
      "orderNumber": 1,
      "restSeconds": 120,
      "workoutSets": [
        { "reps": 8, "weight": 80.0 },
        { "reps": 6, "weight": 85.0 },
        { "reps": 6, "weight": 90.0 }
      ]
    },
    {
      "exerciseId": 2,
      "orderNumber": 2,
      "restSeconds": 90,
      "workoutSets": [
        { "reps": 10, "weight": 40.0 },
        { "reps": 10, "weight": 45.0 },
        { "reps": 8, "weight": 50.0 }
      ]
    },
    {
      "exerciseId": 3,
      "orderNumber": 3,
      "restSeconds": 60,
      "workoutSets": [
        { "reps": 12, "weight": 12.5 },
        { "reps": 12, "weight": 12.5 },
        { "reps": 12, "weight": 12.5 }
      ]
    }
  ]
}
 */
@Service
@RequiredArgsConstructor
@Transactional
public class WorkoutService implements IWorkoutService {

    private final WorkoutTemplateRepository workoutTemplateRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<WorkoutTemplate> getAllWorkoutTemplates(AppUserDetails userDetails) {
        User currentUser = userRepository.findByEmail(userDetails.getUsername());
        return workoutTemplateRepository.findByUserIdOrGlobal(currentUser.getId());
    }

    //Creates WorkoutTemplate -> WorkoutExercise(s) -> WorkoutSet(s)
    //from user request
    @Override
    public WorkoutTemplate addUserWorkoutTemplate(CreateWorkoutTemplateRequest request, AppUserDetails userDetails) {

        //The current user
        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        // Create a new workout template
        WorkoutTemplate template = new WorkoutTemplate();
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setCreatedBy(currentUser);

        //Creates the workouts exercises and sets
        List<WorkoutExercise> exercises = createWorkoutExercises(request, template);
        template.setWorkoutExercises(exercises);

        return workoutTemplateRepository.save(template);
    }

    /// HELPER METHOD
    private List<WorkoutExercise> createWorkoutExercises(CreateWorkoutTemplateRequest request, WorkoutTemplate workoutTemplate) {
        return request.getWorkoutExercises().stream()
                .map(exerciseRequest ->{

                    //Find the exercise from the request
                    Exercise exercise = exerciseRepository.findById(exerciseRequest.getExerciseId())
                            .orElseThrow(() -> new ResourceNotFoundException("exercise not found"));
                    //Create a workoutExercise object
                    WorkoutExercise we = new WorkoutExercise();
                    //Set values
                    we.setExercise(exercise);
                    we.setOrderNumber(exerciseRequest.getOrderNumber());
                    we.setRestSeconds(exerciseRequest.getRestSeconds());
                    we.setWorkoutTemplate(workoutTemplate); //To which templeate does this workoutExercise refer

                    //Create the sets for the exercise
                    List<WorkoutSet> sets = exerciseRequest.getWorkoutSets().stream()
                            .map(setRequest ->{
                                WorkoutSet workoutSet = new WorkoutSet();
                                workoutSet.setWorkoutExercise(we);
                                workoutSet.setReps(setRequest.getReps());
                                workoutSet.setWeight(setRequest.getWeight());
                                workoutSet.setCompleted(false);
                                return workoutSet;
                            })
                            .toList();
                    we.setSets(sets); //Sets the rets
                    return we;
                })
                .toList();
    }

    @Override
    public WorkoutTemplate updateUserWorkoutTemplate(CreateWorkoutTemplateRequest request, AppUserDetails userDetails, Long workoutId) {
        //Find the user
        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        //Find the template:
        WorkoutTemplate workoutTemplate = workoutTemplateRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("workout not found"));

        //Check that the user created the workout
        if(!workoutTemplate.getCreatedBy().equals(currentUser)) {
            throw new ActionNotAllowedException("this workout template trying to be updated, does not belong to the current user");
        }


        workoutTemplate.setName(request.getName());
        workoutTemplate.setDescription(request.getDescription());
        //User Who created the exercise can't be changed

        //Clear the earlier
        workoutTemplate.getWorkoutExercises().clear();

        //'Update' the WorkoutExercises meaning just clear the old one and create a new one
        List<WorkoutExercise> exercises = createWorkoutExercises(request, workoutTemplate);
        workoutTemplate.setWorkoutExercises(exercises);  //Set new exercises

        //save
        return workoutTemplateRepository.save(workoutTemplate);
    }

    @Override
    public void deleteUserWorkoutTemplate(Long workoutId, AppUserDetails userDetails) {
        //Find current user
        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        //Find the workoutTemplate to be deleted:
        WorkoutTemplate template = workoutTemplateRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("workout not found"));

        //Validate
        if(!template.getCreatedBy().equals(currentUser)) {
            throw new ActionNotAllowedException("You are not allowed to delete this workout template");
        }
        //delete workoutTemplate
        workoutTemplateRepository.deleteById(workoutId);
    }


    @Override
    public List<WorkoutTemplateDto> getConvertedWorkoutTemplates(List<WorkoutTemplate> workoutTemplates) {
        return workoutTemplates.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public WorkoutTemplateDto convertToDto(WorkoutTemplate template) {
        //Create the template DTO
        WorkoutTemplateDto templateDto = modelMapper.map(template, WorkoutTemplateDto.class);

        //convert the exercises to exerciseDTO
        List<WorkoutExercise> exercises = template.getWorkoutExercises();

        //Map the exercises
        List<WorkoutExerciseDto> exerciseDtos = exercises.stream()
                .map(exercise -> {
                    WorkoutExerciseDto exerciseDto = modelMapper.map(exercise, WorkoutExerciseDto.class);

                    //Map the sets for each exercise
                    List<WorkoutSetDto> workoutSetDtos = exercise.getSets().stream()
                            .map(set-> modelMapper.map(set, WorkoutSetDto.class))
                            .toList();
                    //For each exercise set the converted workoutsetDTOs
                    exerciseDto.setWorkoutSets(workoutSetDtos);
                    return exerciseDto;
                })
                .toList();

        //Set the converted exercises to the template
        templateDto.setWorkoutExercises(exerciseDtos);
        return templateDto;
    }

}
