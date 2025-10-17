package com.GymApp.GymApp.service.history;

import com.GymApp.GymApp.dto.CompletedSetDto;
import com.GymApp.GymApp.dto.CompletedWorkoutDto;
import com.GymApp.GymApp.exeptions.ActionNotAllowedException;
import com.GymApp.GymApp.exeptions.ResourceNotFoundException;
import com.GymApp.GymApp.model.CompletedSet;
import com.GymApp.GymApp.model.CompletedWorkout;
import com.GymApp.GymApp.model.Exercise;
import com.GymApp.GymApp.model.User;
import com.GymApp.GymApp.repository.CompletedWorkoutRepository;
import com.GymApp.GymApp.repository.ExerciseRepository;
import com.GymApp.GymApp.repository.UserRepository;
import com.GymApp.GymApp.repository.WorkoutTemplateRepository;
import com.GymApp.GymApp.requests.history.SaveCompletedSetRequest;
import com.GymApp.GymApp.requests.history.SaveCompletedWorkoutRequest;
import com.GymApp.GymApp.requests.history.UpdateCompletedSetRequest;
import com.GymApp.GymApp.requests.history.UpdateCompletedWorkoutRequest;
import com.GymApp.GymApp.security.user.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class HistoryService implements IHistoryService {

    private final WorkoutTemplateRepository workoutTemplateRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CompletedWorkoutRepository completedWorkoutRepository;

    @Override
    public List<CompletedWorkoutDto> getCompletedWorkouts(AppUserDetails userDetails) {
        User currentUser = userRepository.findByEmail(userDetails.getUsername());
        if(currentUser==null){
            throw new ResourceNotFoundException("User not found");
        }
        List<CompletedWorkout> completedWorkouts =
                completedWorkoutRepository.findByUserId(currentUser.getId());

        return getConvertedCompletedWorkouts(completedWorkouts);
    }

    /*
    Metod for saving a users completed workout to the database.
    @param requestBody - that contains all info for a completed workout see example in Notion
    @param userDetails - from Spring Security and cookie
    @throws ResourceNotFoundExeption, if the user does not exist or an exercise is not found
    @returns the saved completedWorkoutDto object
     */
    @Override
    public CompletedWorkoutDto saveCompletedWorkout(SaveCompletedWorkoutRequest request, AppUserDetails userDetails) {

        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        if (currentUser == null) {
            throw new ResourceNotFoundException("How did you get here? Log in please");
        }

        //Create CompletedWorkout
        CompletedWorkout completedWorkout = new CompletedWorkout();
        completedWorkout.setUser(currentUser);
        completedWorkout.setCompletedAt(request.getCompletedAt());
        completedWorkout.setDurationSeconds(request.getDurationSeconds());
        completedWorkout.setNotes(request.getNotes());

        //Save the completedSets
        //for all completedSets
        for (SaveCompletedSetRequest setReq : request.getSets()) {
            //Get the exercise completed
            Exercise exercise = exerciseRepository.findById(setReq.getExerciseId()).orElseThrow(
                    () -> new ResourceNotFoundException("Exercise not found"));
            //Create a completed Set
            CompletedSet completedSet = new CompletedSet();
            completedSet.setExercise(exercise);
            completedSet.setReps(setReq.getReps());
            completedSet.setWeight(setReq.getWeight());

            completedWorkout.addSet(completedSet);//add the set to the completedWorkout
        }

        completedWorkoutRepository.save(completedWorkout);
        return convertToDto(completedWorkout);
    }

    /*
    Gets the user, checks that the user exists
    //gets the workout to be deleted
    //Checks that the workout belongs to the user
    //then delete the workout
     */
    @Override
    public void deleteCompletedWorkout(AppUserDetails userDetails, Long completedWorkoutId) {
        User user = userRepository.findByEmail(userDetails.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        CompletedWorkout workoutToBeDeleted =
                completedWorkoutRepository.findByWorkoutId(completedWorkoutId);

        if(!workoutToBeDeleted.getUser().equals(user)){
            throw new ActionNotAllowedException("User not found");
        }
        completedWorkoutRepository.delete(workoutToBeDeleted);

    }
    @Override
    public CompletedWorkoutDto updateCompletedWorkout(UpdateCompletedWorkoutRequest request, AppUserDetails userDetails, Long completedWorkoutId) {
        User currentUser = userRepository.findByEmail(userDetails.getUsername());

        if (currentUser == null) {
            throw new ResourceNotFoundException("How did you get here? Log in please");
        }
        CompletedWorkout completedWorkoutToBeUpdated = completedWorkoutRepository.findByWorkoutId(completedWorkoutId);

        if(!completedWorkoutToBeUpdated.getUser().equals(currentUser)){
            throw new ActionNotAllowedException("User not found");
        }

        completedWorkoutToBeUpdated.setCompletedAt(request.getCompletedAt());
        completedWorkoutToBeUpdated.setDurationSeconds(request.getDurationSeconds());
        completedWorkoutToBeUpdated.setNotes(request.getNotes());

        completedWorkoutToBeUpdated.getSets().clear();

        for(UpdateCompletedSetRequest setReq : request.getSets()){
            Exercise exercise = exerciseRepository.findById(setReq.getExerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id : "+setReq.getExerciseId()));
            CompletedSet set = new CompletedSet();
            set.setExercise(exercise);
            set.setReps(setReq.getReps());
            set.setWeight(setReq.getWeight());
            set.setOrderNumber(setReq.getOrderNumber());
            set.setRestSeconds(setReq.getRestSeconds());

            completedWorkoutToBeUpdated.addSet(set);
        }

        CompletedWorkout UpdatedCompletedWorkout =
                completedWorkoutRepository.save(completedWorkoutToBeUpdated);

        return convertToDto(UpdatedCompletedWorkout);

    }

    public List<CompletedWorkoutDto> getConvertedCompletedWorkouts(List<CompletedWorkout> completedWorkouts) {
        return completedWorkouts.stream()
                .map(this::convertToDto)
                .toList();
    }

    public CompletedWorkoutDto convertToDto(CompletedWorkout completedWorkout) {
        CompletedWorkoutDto dto = modelMapper.map(completedWorkout, CompletedWorkoutDto.class);
        dto.setUserId(completedWorkout.getUser().getId());

        dto.setUserId(completedWorkout.getUser().getId());
        dto.getSets().forEach(setDto -> {
            CompletedSet set = completedWorkout.getSets()
                    .stream()
                    .filter(s -> s.getId().equals(setDto.getId()))
                    .findFirst()
                    .orElse(null);
            if (set != null) {
                setDto.setExerciseId(set.getExercise().getId());
                setDto.setExerciseName(set.getExercise().getName());
            }
        });

        return dto;
    }
}
