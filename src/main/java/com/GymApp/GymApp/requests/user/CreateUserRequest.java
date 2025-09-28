package com.GymApp.GymApp.requests.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
