package com.izepon.task_manager.controller;

import com.izepon.task_manager.dto.TaskDto;
import com.izepon.task_manager.dto.TaskDto.TaskResponse;
import com.izepon.task_manager.entity.Task;
import com.izepon.task_manager.enums.Status;
import com.izepon.task_manager.service.TaskService;
import com.izepon.task_manager.util.TaskSpecification;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskDto.TaskCreateRequest request) {

        Task createdTask = taskService.createTask(request);
        return new ResponseEntity<>(TaskResponse.fromEntity(createdTask), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> findTasks(@RequestParam(required = false) Status status,
                                                        @RequestParam(required = false) UUID userId, Pageable pageable) {

        Specification<Task> spec = TaskSpecification.withFilters(status, userId);
        Page<Task> taskPage = taskService.findTasks(spec, pageable);
        return ResponseEntity.ok(taskPage.map(TaskResponse::fromEntity));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable UUID id, @Valid @RequestBody TaskDto.TaskStatusUpdateRequest request) {

        Task updatedTask = taskService.updateStatus(id, request.status());
        return ResponseEntity.ok(TaskResponse.fromEntity(updatedTask));
    }
}
