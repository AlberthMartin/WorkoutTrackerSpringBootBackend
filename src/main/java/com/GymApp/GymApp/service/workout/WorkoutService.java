package com.GymApp.GymApp.service.workout;


import com.GymApp.GymApp.dto.ExerciseDto;
import com.GymApp.GymApp.dto.WorkoutExerciseDto;
import com.GymApp.GymApp.dto.WorkoutSetDto;
import com.GymApp.GymApp.dto.WorkoutTemplateDto;
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

import java.nio.charset.Charset;
import java.util.List;

/*
This is something we want from the frontend
{
  "name": "Upper Body 1",
  "description": "Push/pull workout",
  "exercises": [
    {
      "exerciseId": 685fa4ac97a4309a9a8cb39e,
      "orderNumber": 1,
      "restSeconds": 90,
      "sets": [
        { "reps": 10, "weight": 50 },
        { "reps": 10, "weight": 50 },
        { "reps": 10, "weight": 55 }
      ]
    },
    {
      "exerciseId": 686aab6f31ba6904bcf0d742,
      "orderNumber": 2,
      "restSeconds": 60,
      "sets": [
        { "reps": 12, "weight": 30 },
        { "reps": 12, "weight": 30 }
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
        WorkoutTemplate template = new  WorkoutTemplate();
        template.setName(request.getName());
        template.setDescription(request.getDescription());
        template.setCreatedBy(currentUser);

        //Create the WorkoutExercises
        List<WorkoutExercise> exercises = request.getWorkoutExercises().stream()
                .map(exerciseRequest -> {
                    //Fetch the exercise
                    Exercise exercise = exerciseRepository.findById(exerciseRequest.getExerciseId())
                            .orElseThrow(() -> new ResourceNotFoundException("exercise not found"));

                    //Create workoutExercise Object and set values
                    WorkoutExercise we = new WorkoutExercise();
                    we.setExercise(exercise);
                    we.setOrderNumber(exerciseRequest.getOrderNumber());
                    we.setRestSeconds(exerciseRequest.getRestSeconds());

                    //Create the sets
                    List<WorkoutSet> sets = exerciseRequest.getWorkoutSets().stream()
                            .map(setReq ->{
                                WorkoutSet workoutSet = new WorkoutSet();
                                workoutSet.setWorkoutExercise(we);
                                workoutSet.setReps(setReq.getReps());
                                workoutSet.setWeight(setReq.getWeight());
                                workoutSet.setCompleted(false);
                                return workoutSet;
                            })
                            .toList();
                    we.setSets(sets);
                    return we;
                })
                .toList();
        template.setWorkoutExercises(exercises);

        WorkoutTemplate saved;
        saved = workoutTemplateRepository.save(template);

        return saved;
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
