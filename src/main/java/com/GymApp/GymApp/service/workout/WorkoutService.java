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
import com.GymApp.GymApp.requests.workout.CreateWorkoutExerciseRequest;
import com.GymApp.GymApp.requests.workout.CreateWorkoutSetRequest;
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

    //CreateWorkoutTemplateRequest -> List<CreateExerciseRequest> -> List<CreateWorkoutSetRequest>
    @Override
    public WorkoutTemplate addUserWorkoutTemplateV2(CreateWorkoutTemplateRequest request, AppUserDetails userDetails) {

        //Get current user:
        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        //Create e template
        WorkoutTemplate template = new WorkoutTemplate();
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setCreatedBy(currentUser);

        //Loop through exercises from frontend request,
        for(CreateWorkoutExerciseRequest exReq : request.getWorkoutExercises()){

            //Find exercise
            Exercise exercise = exerciseRepository.findById(exReq.getExerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

            //create a workoutExercise with workoutSets from REQUEST
            WorkoutExercise we = mapToWorkoutExerciseWithWorkoutSets(exReq, exercise);

            //add exercise to template
            template.addExercise(we); //Entity method
        }
        return workoutTemplateRepository.save(template);
    }

       /// HELPER METHOD to create a workoutExercise with workoutSets from REQUEST
    private WorkoutExercise mapToWorkoutExerciseWithWorkoutSets(CreateWorkoutExerciseRequest request, Exercise exercise) {
        WorkoutExercise we = new WorkoutExercise();
            we.setExercise(exercise);
            we.setOrderNumber(request.getOrderNumber());
            we.setRestSeconds(request.getRestSeconds());

            //Loop through sets for this exercise
            for(CreateWorkoutSetRequest setReq : request.getWorkoutSets()){
                WorkoutSet ws = new WorkoutSet();
                ws.setReps(setReq.getReps());
                ws.setWeight(setReq.getWeight());
                ws.setCompleted(false);

                we.addSet(ws); //entity method takes care of relationship
            }
        return we;
    }

    @Override
    public WorkoutTemplate updateUserWorkoutTemplateV2(CreateWorkoutTemplateRequest request, AppUserDetails userDetails, Long workoutId) {
        //Find user
        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        //Find template
        WorkoutTemplate template = workoutTemplateRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));

        //Check owner
        if(!template.getCreatedBy().equals(currentUser)) {
            throw new ActionNotAllowedException("You are not allowed to update this workout template");
        }
        //Update basic info
        template.setName(request.getName());
        template.setDescription(request.getDescription());

        //Clear old exercises (Because it is just a blueprint)
        template.getWorkoutExercises().clear();

        //Rebuild exercises & sets, remember CreateWorkoutTemplateRequest has a list of the
        //exercises to be added List<CreateWorkoutExerciseRequest>
        for(CreateWorkoutExerciseRequest exReq : request.getWorkoutExercises()){
            Exercise exercise = exerciseRepository.findById(exReq.getExerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));

            WorkoutExercise we = mapToWorkoutExerciseWithWorkoutSets(exReq, exercise);
            template.addExercise(we); //Entity method
        }
        return workoutTemplateRepository.save(template);
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
