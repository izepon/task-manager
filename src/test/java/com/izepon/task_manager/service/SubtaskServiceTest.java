package com.izepon.task_manager.service;

import com.izepon.task_manager.dto.SubtaskDto.SubtaskCreateRequest;
import com.izepon.task_manager.entity.Subtask;
import com.izepon.task_manager.entity.Task;
import com.izepon.task_manager.enums.Status;
import com.izepon.task_manager.exception.ResourceNotFoundException;
import com.izepon.task_manager.repository.SubtaskRepository;
import com.izepon.task_manager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubtaskServiceTest {

    @Mock
    private SubtaskRepository subtaskRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private SubtaskService subtaskService;

    private final static String SUBTASK_TITLE = "Create entities";
    private final static String DESCRIPTION = "Subtasks descriptions";

    @Test
    void createSubtask_WhenParentTaskExists_ShouldCreateSubtaskSuccessfully() {
        var taskId = UUID.randomUUID();
        var parentTask = new Task();
        parentTask.setId(taskId);
        var request = new SubtaskCreateRequest(SUBTASK_TITLE, DESCRIPTION);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(parentTask));
        when(subtaskRepository.save(any(Subtask.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subtask result = subtaskService.createSubtask(taskId, request);

        assertNotNull(result);
        assertEquals(request.title(), result.getTitle());
        assertEquals(Status.PENDING, result.getStatus());
        assertEquals(parentTask, result.getTask());
    }

    @Test
    void createSubtask_WhenParentTaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        var taskId = UUID.randomUUID();
        var request = new SubtaskCreateRequest(SUBTASK_TITLE, DESCRIPTION);
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subtaskService.createSubtask(taskId, request);
        });
        verify(subtaskRepository, never()).save(any(Subtask.class));
    }

    @Test
    void updateSubtaskStatus_ToCompleted_ShouldSetConclusionDate() {
        var subtaskId = UUID.randomUUID();
        var subtask = new Subtask();
        subtask.setId(subtaskId);
        subtask.setStatus(Status.PENDING);

        when(subtaskRepository.findById(subtaskId)).thenReturn(Optional.of(subtask));

        Subtask result = subtaskService.updateSubtaskStatus(subtaskId, Status.COMPLETED);

        assertEquals(Status.COMPLETED, result.getStatus());
        assertNotNull(result.getConcludedAt());
    }

    @Test
    void updateSubtaskStatus_WhenSubtaskDoesNotExist_ShouldThrowResourceNotFoundException() {
        var subtaskId = UUID.randomUUID();
        when(subtaskRepository.findById(subtaskId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subtaskService.updateSubtaskStatus(subtaskId, Status.IN_PROGRESS);
        });
    }
}