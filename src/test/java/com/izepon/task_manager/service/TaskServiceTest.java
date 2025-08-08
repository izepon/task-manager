package com.izepon.task_manager.service;

import com.izepon.task_manager.dto.TaskDto.TaskCreateRequest;
import com.izepon.task_manager.entity.Task;
import com.izepon.task_manager.entity.User;
import com.izepon.task_manager.enums.Status;
import com.izepon.task_manager.exception.BusinessRuleException;
import com.izepon.task_manager.exception.ResourceNotFoundException;
import com.izepon.task_manager.repository.TaskRepository;
import com.izepon.task_manager.repository.UserRepository;
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
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private final static String TASK_TITLE = "New API";
    private final static String DESCRIPTION = "Build the new API";

    @Test
    void createTask_WhenUserExists_ShouldCreateTaskSuccessfully() {
        var userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        var request = new TaskCreateRequest(TASK_TITLE, DESCRIPTION, userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals(request.title(), result.getTitle());
        assertEquals(Status.PENDING, result.getStatus());
        assertEquals(user, result.getUser());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void createTask_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        var userId = UUID.randomUUID();
        var request = new TaskCreateRequest(TASK_TITLE, DESCRIPTION, userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.createTask(request);
        });
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateStatus_ToCompleted_WhenSubtasksAreCompleted_ShouldSucceed() {
        var taskId = UUID.randomUUID();
        var task = new Task();
        task.setId(taskId);
        task.setStatus(Status.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.existsBySubtasks_Task_IdAndSubtasks_StatusNot(taskId, Status.COMPLETED)).thenReturn(false);

        Task result = taskService.updateStatus(taskId, Status.COMPLETED);

        assertEquals(Status.COMPLETED, result.getStatus());
        assertNotNull(result.getConcludedAt());
    }

    @Test
    void updateStatus_ToCompleted_WhenSubtasksArePending_ShouldThrowBusinessRuleException() {
        var taskId = UUID.randomUUID();
        var task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.existsBySubtasks_Task_IdAndSubtasks_StatusNot(taskId, Status.COMPLETED)).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> {
            taskService.updateStatus(taskId, Status.COMPLETED);
        });
    }

    @Test
    void updateStatus_FromCompletedToPending_ShouldClearConclusionDate() {
        var taskId = UUID.randomUUID();
        var task = new Task();
        task.setId(taskId);
        task.setStatus(Status.COMPLETED);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Task result = taskService.updateStatus(taskId, Status.PENDING);

        assertEquals(Status.PENDING, result.getStatus());
        assertNull(result.getConcludedAt());
    }
}