package com.izepon.task_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izepon.task_manager.dto.TaskDto.TaskCreateRequest;
import com.izepon.task_manager.dto.TaskDto.TaskStatusUpdateRequest;
import com.izepon.task_manager.entity.Task;
import com.izepon.task_manager.entity.User;
import com.izepon.task_manager.enums.Status;
import com.izepon.task_manager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID TASK_ID = UUID.randomUUID();
    private static final String TASK_TITLE = "Title task";
    private static final String TASK_DESCRIPTION = "Task description for test";

    @Test
    @WithMockUser
    void createTask_WithValidData_ShouldReturnCreated() throws Exception {
        var request = new TaskCreateRequest(TASK_TITLE, TASK_DESCRIPTION, USER_ID);
        var user = new User();
        user.setId(USER_ID);
        var createdTask = new Task();
        createdTask.setId(TASK_ID);
        createdTask.setTitle(TASK_TITLE);
        createdTask.setUser(user);

        when(taskService.createTask(any(TaskCreateRequest.class))).thenReturn(createdTask);

        mockMvc.perform(post("/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(TASK_ID.toString()))
                .andExpect(jsonPath("$.title").value(TASK_TITLE))
                .andExpect(jsonPath("$.userId").value(USER_ID.toString()));
    }

    @Test
    @WithMockUser
    void findTasks_ShouldReturnPageOfTasks() throws Exception {
        var user = new User();
        user.setId(USER_ID);
        var task = new Task();
        task.setId(TASK_ID);
        task.setTitle(TASK_TITLE);
        task.setUser(user);
        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskService.findTasks(any(), any(Pageable.class))).thenReturn(taskPage);

        mockMvc.perform(get("/tasks")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(TASK_ID.toString()))
                .andExpect(jsonPath("$.content[0].title").value(TASK_TITLE));
    }

    @Test
    @WithMockUser
    void updateStatus_WithValidData_ShouldReturnOk() throws Exception {
        var request = new TaskStatusUpdateRequest(Status.IN_PROGRESS);
        var user = new User();
        user.setId(USER_ID);
        var updatedTask = new Task();
        updatedTask.setId(TASK_ID);
        updatedTask.setStatus(Status.IN_PROGRESS);
        updatedTask.setUser(user);

        when(taskService.updateStatus(TASK_ID, Status.IN_PROGRESS)).thenReturn(updatedTask);

        mockMvc.perform(patch("/tasks/{id}/status", TASK_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TASK_ID.toString()))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }
}