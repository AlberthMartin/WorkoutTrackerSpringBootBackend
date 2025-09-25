package com.GymApp.GymApp.service.exercise;

import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.requests.exercise.AddExerciseRequest;
import com.GymApp.GymApp.requests.exercise.UpdateExerciseRequest;

import java.util.List;

public interface IExerciseService {
    Exercise addExercise(AddExerciseRequest request);
    Exercise getProductById(Long id);
    void deleteExerciseById(Long exerciseId);
    Exercise updateExercise(UpdateExerciseRequest request);

    List<Exercise> getAllExercises();
    List<Exercise> getExercisesByPrimaryMuscleGroup(MuscleGroup muscleGroup);
    List<Exercise> getExercisesByExerciseType(ExerciseType exerciseType);
    List<Exercise> getExercisesByName(String name);
}
