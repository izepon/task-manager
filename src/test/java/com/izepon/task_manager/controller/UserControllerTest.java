package com.izepon.task_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izepon.task_manager.dto.UserDto.UserCreateRequest;
import com.izepon.task_manager.entity.User;
import com.izepon.task_manager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private static final String USER_NAME = "User test";
    private static final String USER_EMAIL = "test@example.com";

    @Test
    @WithMockUser
    void createUser_WithValidData_ShouldReturnCreated() throws Exception {
        var request = new UserCreateRequest(USER_NAME, USER_EMAIL);
        var createdUser = new User();
        createdUser.setId(UUID.randomUUID());
        createdUser.setName(USER_NAME);
        createdUser.setEmail(USER_EMAIL);

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(createdUser);

        mockMvc.perform(post("/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdUser.getId().toString()))
                .andExpect(jsonPath("$.name").value(USER_NAME))
                .andExpect(jsonPath("$.email").value(USER_EMAIL));
    }

    @Test
    @WithMockUser
    void findUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        var userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        user.setName(USER_NAME);
        user.setEmail(USER_EMAIL);

        when(userService.findUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value(USER_NAME));
    }
}