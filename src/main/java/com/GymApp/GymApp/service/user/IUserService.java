package com.GymApp.GymApp.service.user;

import com.GymApp.GymApp.dto.UserDto;
import com.GymApp.GymApp.model.User;
import com.GymApp.GymApp.requests.user.CreateUserRequest;
import com.GymApp.GymApp.requests.user.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUserById(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();

}
