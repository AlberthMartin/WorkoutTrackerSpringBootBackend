package com.GymApp.GymApp.requests.user;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotBlank
    private String username;
}
