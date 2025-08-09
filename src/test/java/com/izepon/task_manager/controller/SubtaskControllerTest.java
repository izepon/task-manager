package com.izepon.task_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izepon.task_manager.dto.SubtaskDto.SubtaskCreateRequest;
import com.izepon.task_manager.dto.SubtaskDto.SubtaskStatusUpdateRequest;
import com.izepon.task_manager.entity.Subtask;
import com.izepon.task_manager.entity.Task;
import com.izepon.task_manager.enums.Status;
import com.izepon.task_manager.service.SubtaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;


@WebMvcTest(SubtaskController.class)
class SubtaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubtaskService subtaskService;

    private static final UUID TASK_ID = UUID.randomUUID();
    private static final UUID SUBTASK_ID = UUID.randomUUID();
    private static final String SUBTASK_TITLE = "Title test";

    @Test
    @WithMockUser
    void createSubtask_WithValidData_ShouldReturnCreated() throws Exception {
        var request = new SubtaskCreateRequest(SUBTASK_TITLE, "Cover all services");
        var parentTask = new Task();
        parentTask.setId(TASK_ID);
        var createdSubtask = new Subtask();
        createdSubtask.setId(SUBTASK_ID);
        createdSubtask.setTitle(SUBTASK_TITLE);
        createdSubtask.setTask(parentTask);

        when(subtaskService.createSubtask(any(UUID.class), any(SubtaskCreateRequest.class))).thenReturn(createdSubtask);

        mockMvc.perform(post("/tasks/{taskId}/subtasks", TASK_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(SUBTASK_ID.toString()))
                .andExpect(jsonPath("$.title").value(SUBTASK_TITLE))
                .andExpect(jsonPath("$.taskId").value(TASK_ID.toString()));
    }

    @Test
    @WithMockUser
    void getSubtasksByTaskId_ShouldReturnListOfSubtasks() throws Exception {
        var parentTask = new Task();
        parentTask.setId(TASK_ID);
        var subtask = new Subtask();
        subtask.setId(SUBTASK_ID);
        subtask.setTitle(SUBTASK_TITLE);
        subtask.setTask(parentTask);

        when(subtaskService.findSubtasksByTaskId(TASK_ID)).thenReturn(List.of(subtask));

        mockMvc.perform(get("/tasks/{taskId}/subtasks", TASK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(SUBTASK_ID.toString())))
                .andExpect(jsonPath("$[0].title", is(SUBTASK_TITLE)));
    }

    @Test
    @WithMockUser
    void updateSubtaskStatus_WithValidData_ShouldReturnOk() throws Exception {
        var request = new SubtaskStatusUpdateRequest(Status.COMPLETED);
        var parentTask = new Task();
        parentTask.setId(TASK_ID);
        var updatedSubtask = new Subtask();
        updatedSubtask.setId(SUBTASK_ID);
        updatedSubtask.setStatus(Status.COMPLETED);
        updatedSubtask.setTask(parentTask);

        when(subtaskService.updateSubtaskStatus(SUBTASK_ID, Status.COMPLETED)).thenReturn(updatedSubtask);

        mockMvc.perform(patch("/subtasks/{subtaskId}/status", SUBTASK_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SUBTASK_ID.toString()))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}