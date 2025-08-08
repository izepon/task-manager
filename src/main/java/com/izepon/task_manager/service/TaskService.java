package com.izepon.task_manager.service;

import com.izepon.task_manager.dto.TaskDto.TaskCreateRequest;
import com.izepon.task_manager.entity.Task;
import com.izepon.task_manager.entity.User;
import com.izepon.task_manager.enums.Status;
import com.izepon.task_manager.exception.BusinessRuleException;
import com.izepon.task_manager.exception.ResourceNotFoundException;
import com.izepon.task_manager.repository.TaskRepository;
import com.izepon.task_manager.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Task createTask(TaskCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.userId()));

        Task newTask = new Task();
        newTask.setTitle(request.title());
        newTask.setDescription(request.description());
        newTask.setUser(user);
        newTask.setStatus(Status.PENDING);

        return taskRepository.save(newTask);
    }

    @Transactional(readOnly = true)
    public Page<Task> findTasks(Specification<Task> spec, Pageable pageable) {
        return taskRepository.findAll(spec, pageable);
    }

    @Transactional
    public Task updateStatus(UUID taskId, Status newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        if (newStatus == Status.COMPLETED) {
            boolean hasNonCompletedSubtasks = taskRepository.existsBySubtasks_Task_IdAndSubtasks_StatusNot(taskId, Status.COMPLETED);
            if (hasNonCompletedSubtasks) {
                throw new BusinessRuleException("Task cannot be completed because there are pending or in-progress subtasks.");
            }
            task.setConcludedAt(LocalDateTime.now());
        } else {
            task.setConcludedAt(null);
        }

        task.setStatus(newStatus);
        return task;
    }
}
