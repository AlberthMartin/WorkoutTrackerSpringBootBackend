package com.GymApp.GymApp.data;

import com.GymApp.GymApp.enums.ExerciseType;
import com.GymApp.GymApp.enums.MuscleGroup;
import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.Role;
import com.GymApp.GymApp.model.User;
import com.GymApp.GymApp.repository.ExerciseRepository;
import com.GymApp.GymApp.repository.RoleRepository;
import com.GymApp.GymApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.GymApp.GymApp.enums.ExerciseType.BARBELL;
import static com.GymApp.GymApp.enums.MuscleGroup.CHEST;
import static com.GymApp.GymApp.enums.MuscleGroup.TRICEPS;

@Transactional
@Component
@RequiredArgsConstructor
public class dataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ExerciseRepository exerciseRepository;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");

        createDefaultUsersIfNotExist(defaultRoles);
        createDefaultUsersIfNotExist();
        createDefaultAdminIfNotExist();
        createDefaultGlobalExercisesIfNotExist();
    }


    private void createDefaultUsersIfNotExist() {
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        ;
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "user" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setUsername("User" + i);
            user.setEmail(defaultEmail);
            user.setRoles(Set.of(userRole));
            user.setPassword(passwordEncoder.encode("123456"));
            userRepository.save(user);
            System.out.println("Default user" + i + "created successfully");
        }
    }

    private void createDefaultAdminIfNotExist() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        ;

        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@gmail.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setUsername("Admin" + i);

            user.setEmail(defaultEmail);
            user.setRoles(Set.of(adminRole));
            user.setPassword(passwordEncoder.encode("123456"));
            userRepository.save(user);
            System.out.println("Default admin" + i + "created successfully");
        }
    }


    private void createDefaultUsersIfNotExist(Set<String> roles) {
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }

    private void createDefaultGlobalExercisesIfNotExist() {
        if(exerciseRepository.count() == 0) {
            List<Exercise> exercises = List.of(
                new Exercise("Bench Press", "Chest pressing movement with a barbell", CHEST, TRICEPS, BARBELL, null),
                new Exercise("Incline Dumbbell Press",
                             "Pressing movement on an incline bench with dumbbells",
                             MuscleGroup.CHEST,
                             MuscleGroup.SHOULDERS,
                             ExerciseType.DUMBBELL,
                             null),

                new Exercise("Back Squat",
                             "Compound movement for legs with barbell on back",
                             MuscleGroup.QUADRICEPS,
                             MuscleGroup.GLUTES,
                             ExerciseType.BARBELL,
                             null),

                new Exercise("Deadlift",
                             "Full body pulling exercise with barbell",
                             MuscleGroup.BACK,
                             MuscleGroup.HAMSTRINGS,
                             ExerciseType.BARBELL,
                             null),

                new Exercise("Overhead Press",
                             "Shoulder pressing movement with barbell",
                             MuscleGroup.SHOULDERS,
                             MuscleGroup.TRICEPS,
                             ExerciseType.BARBELL,
                             null),

                new Exercise("Bicep Curl",
                             "Isolation movement for the biceps using dumbbells",
                             MuscleGroup.BICEPS,
                             null,
                             ExerciseType.DUMBBELL,
                             null),

                new Exercise("Triceps Pushdown",
                             "Cable isolation exercise for triceps",
                             MuscleGroup.TRICEPS,
                             null,
                             ExerciseType.MACHINE,
                             null)
            );
            exerciseRepository.saveAll(exercises);
            System.out.println("Example exercises inserted into database");
        }

    }
}
