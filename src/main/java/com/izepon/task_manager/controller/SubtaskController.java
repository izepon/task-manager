package com.izepon.task_manager.controller;

import com.izepon.task_manager.dto.SubtaskDto;
import com.izepon.task_manager.dto.SubtaskDto.SubtaskResponse;
import com.izepon.task_manager.entity.Subtask;
import com.izepon.task_manager.service.SubtaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class SubtaskController {

    private final SubtaskService subtaskService;

    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    @PostMapping("/tasks/{taskId}/subtasks")
    public ResponseEntity<SubtaskResponse> createSubtask(@PathVariable UUID taskId, @Valid @RequestBody SubtaskDto.SubtaskCreateRequest request) {

        Subtask createdSubtask = subtaskService.createSubtask(taskId, request);
        return new ResponseEntity<>(SubtaskResponse.fromEntity(createdSubtask), HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{taskId}/subtasks")
    public ResponseEntity<List<SubtaskResponse>> getSubtasksByTaskId(@PathVariable UUID taskId) {

        List<Subtask> subtasks = subtaskService.findSubtasksByTaskId(taskId);
        List<SubtaskResponse> response = subtasks.stream()
                .map(SubtaskResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/subtasks/{subtaskId}/status")
    public ResponseEntity<SubtaskResponse> updateSubtaskStatus(@PathVariable UUID subtaskId,
                                                               @Valid @RequestBody SubtaskDto.SubtaskStatusUpdateRequest request) {

        Subtask updatedSubtask = subtaskService.updateSubtaskStatus(subtaskId, request.status());
        return ResponseEntity.ok(SubtaskResponse.fromEntity(updatedSubtask));
    }
}
