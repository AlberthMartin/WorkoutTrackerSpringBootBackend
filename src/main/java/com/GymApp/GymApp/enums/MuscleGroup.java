package com.GymApp.GymApp.enums;


public enum MuscleGroup {
    CHEST("Chest"),
    BACK("Back"),
    SHOULDERS("Shoulders"),
    TRICEPS("Triceps"),
    BICEPS("Biceps"),
    FOREARMS("Forearms"),
    CORE("Core"),
    QUADRICEPS("Quadriceps"),
    HAMSTRINGS("Hamstrings"),
    GLUTES("Glutes"),
    CALVES("Calves");

    private final String label;

    MuscleGroup(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}


