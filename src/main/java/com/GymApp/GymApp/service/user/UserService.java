package com.GymApp.GymApp.service.user;

import com.GymApp.GymApp.dto.UserDto;
import com.GymApp.GymApp.exeptions.AlreadyExistsException;
import com.GymApp.GymApp.exeptions.ResourceNotFoundException;
import com.GymApp.GymApp.model.User;
import com.GymApp.GymApp.repository.UserRepository;
import com.GymApp.GymApp.requests.user.CreateUserRequest;
import com.GymApp.GymApp.requests.user.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request) //can be null
                .filter(user -> !userRepository.existsByEmail(request.getEmail())) //Checks that user is not in the database
                        .map(req -> {
                            //Create the user
                            User user = new User();
                            user.setUsername(request.getUsername());
                            user.setEmail(request.getEmail());
                            user.setPassword(passwordEncoder.encode(request.getPassword()));
                            return userRepository.save(user); // save user

                        }).orElseThrow(()-> new AlreadyExistsException(request.getEmail() + " already exists"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> { //find user
            existingUser.setUsername(request.getUsername()); //set new username
            return userRepository.save(existingUser); //save user
        }).orElseThrow(()-> new ResourceNotFoundException("User not found")); //the user didn't exist
    }

    @Override
    public void deleteUserById(Long userId) {
        //Delete of present else throw error
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete,
                ()->{throw new ResourceNotFoundException("User not found");});
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
