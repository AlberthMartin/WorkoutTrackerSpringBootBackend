package com.GymApp.GymApp.enums;

public enum ExerciseType {
    BARBELL("Barbell"),
    DUMBBELL("Dumbbell"),
    MACHINE("Machine"),
    BODYWEIGHT("Bodyweight"),
    ASSISTED_BODYWEIGHT("Assisted Bodyweight"),
    CARDIO("Cardio"),
    DURATION("Duration");

    private final String displayName;

    ExerciseType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
