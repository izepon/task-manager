package com.izepon.task_manager.dto;

import com.izepon.task_manager.entity.Task;
import com.izepon.task_manager.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class TaskDto {

    public record TaskCreateRequest(
            @NotBlank String title,
            String description,
            @NotNull UUID userId
    ) {}

    public record TaskStatusUpdateRequest(
            @NotNull Status status
    ) {}

    public record TaskResponse(
            UUID id,
            String title,
            String description,
            Status status,
            LocalDateTime createdAt,
            LocalDateTime concludedAt,
            UUID userId
    ) {
        public static TaskResponse fromEntity(Task task) {
            return new TaskResponse(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getCreatedAt(),
                    task.getConcludedAt(),
                    task.getUser().getId()
            );
        }
    }
}
