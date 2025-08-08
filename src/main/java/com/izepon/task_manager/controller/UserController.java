package com.izepon.task_manager.controller;

import com.izepon.task_manager.dto.UserDto;
import com.izepon.task_manager.dto.UserDto.UserResponse;
import com.izepon.task_manager.entity.User;
import com.izepon.task_manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserDto.UserCreateRequest request) {
        User createdUser = userService.createUser(request);
        return new ResponseEntity<>(UserResponse.fromEntity(createdUser), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable UUID id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }
}
