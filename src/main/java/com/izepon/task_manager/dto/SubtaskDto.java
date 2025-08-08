package com.izepon.task_manager.dto;

import com.izepon.task_manager.entity.Subtask;
import com.izepon.task_manager.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class SubtaskDto {

    public record SubtaskCreateRequest(
            @NotBlank String title,
            String description
    ) {}

    public record SubtaskStatusUpdateRequest(
            @NotNull Status status
    ) {}

    public record SubtaskResponse(
            UUID id,
            String title,
            String description,
            Status status,
            LocalDateTime createdAt,
            LocalDateTime concludedAt,
            UUID taskId
    ) {
        public static SubtaskResponse fromEntity(Subtask subtask) {
            return new SubtaskResponse(
                    subtask.getId(),
                    subtask.getTitle(),
                    subtask.getDescription(),
                    subtask.getStatus(),
                    subtask.getCreatedAt(),
                    subtask.getConcludedAt(),
                    subtask.getTask().getId()
            );
        }
    }
}
