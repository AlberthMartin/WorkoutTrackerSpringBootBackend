package com.GymApp.GymApp.exeptions;

public class ExerciseAlreadyExistsException extends RuntimeException {
    public ExerciseAlreadyExistsException(String message) {
        super(message);
    }
}
