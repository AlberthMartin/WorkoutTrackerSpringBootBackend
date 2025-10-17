package com.GymApp.GymApp.service;

import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.User;
import com.GymApp.GymApp.repository.ExerciseRepository;
import com.GymApp.GymApp.repository.UserRepository;
import com.GymApp.GymApp.requests.exercise.AddExerciseRequest;
import com.GymApp.GymApp.security.user.AppUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExerciseService {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppUserDetails userDetails;

    @InjectMocks
    private ExerciseService exerciseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userDetails.getUsername()).thenReturn("test@example.com");
    }
/*
    @Test
    void addExercise_asUser_shouldSetCreatedBy() {
        // given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        AddExerciseRequest request = new AddExerciseRequest();
        request.setName("Bench Press");
        request.setDescription("Chest exercise");

        Exercise savedExercise = new Exercise();
        savedExercise.setName("Bench Press");
        savedExercise.setCreatedBy(user);
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(savedExercise);

        // when
        Exercise result = exerciseService.addExercise(request, userDetails, false);

        // then
        assertEquals("Bench Press", result.getName());
        assertEquals(user, result.getCreatedBy());
        verify(exerciseRepository).save(any(Exercise.class));
    }

    @Test
    void addExercise_asAdmin_shouldSetCreatedByNull() {
        // given
        AddExerciseRequest request = new AddExerciseRequest();
        request.setName("Squat");

        Exercise savedExercise = new Exercise();
        savedExercise.setName("Squat");
        savedExercise.setCreatedBy(null);
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(savedExercise);

        // when
        Exercise result = exerciseService.addExercise(request, userDetails, true);

        // then
        assertEquals("Squat", result.getName());
        assertNull(result.getCreatedBy());
        verify(exerciseRepository).save(any(Exercise.class));
    }

 */
}
