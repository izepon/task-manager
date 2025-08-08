package com.izepon.task_manager.dto;

import com.izepon.task_manager.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class UserDto {

    public record UserCreateRequest(
            @NotBlank String name,
            @NotBlank @Email String email
    ) {}

    public record UserResponse(UUID id, String name, String email) {
        public static UserResponse fromEntity(User user) {
            return new UserResponse(user.getId(), user.getName(), user.getEmail());
        }
    }
}
