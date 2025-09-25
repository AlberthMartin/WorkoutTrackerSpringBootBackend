package com.GymApp.GymApp.service.exercise;

import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import com.GymApp.GymApp.exeptions.ResourceNotFoundException;
import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.User;
import com.GymApp.GymApp.repository.ExerciseRepository;
import com.GymApp.GymApp.requests.exercise.AddExerciseRequest;
import com.GymApp.GymApp.requests.exercise.UpdateExerciseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService implements IExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Override
    public Exercise addExercise(AddExerciseRequest request) {
        /*
        User user = Optional.ofNullable(userRepository.findByName(request.getUser().getRole()))
         */
        return null;
    }
    /*
    private Long id;
    private User createdBy;
    private String name;
    private String description;
    private MuscleGroup primaryMuscleGroup;
    private MuscleGroup secondaryMuscleGroup;
    private ExerciseType exerciseType;
     */
    //Helper for addExercise
    private Exercise createExercise(AddExerciseRequest request, User user) {
        return new Exercise(
                request.getName(),
                request.getDescription(),
                request.getPrimaryMuscleGroup(),
                request.getSecondaryMuscleGroup(),
                request.getExerciseType(),
                user
        );
    }

    @Override
    public Exercise getProductById(Long exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));
    }

    @Override
    public void deleteExerciseById(Long exerciseId) {
        exerciseRepository.findById(exerciseId)
                .ifPresentOrElse(exerciseRepository::delete,
                        () -> new ResourceNotFoundException("Exercise not found"));
    }

    @Override
    public Exercise updateExercise(UpdateExerciseRequest request) {
        return null;
    }

    @Override
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    @Override
    public List<Exercise> getExercisesByPrimaryMuscleGroup(MuscleGroup muscleGroup) {
        return exerciseRepository.findByPrimaryMuscleGroup(muscleGroup);
    }

    @Override
    public List<Exercise> getExercisesByExerciseType(ExerciseType exerciseType) {
        return exerciseRepository.findByExerciseType(exerciseType);
    }

    @Override
    public List<Exercise> getExercisesByName(String name) {
        return exerciseRepository.findByName(name);
    }
}
