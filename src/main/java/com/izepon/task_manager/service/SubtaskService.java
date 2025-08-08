package com.izepon.task_manager.service;

import com.izepon.task_manager.dto.SubtaskDto.SubtaskCreateRequest;
import com.izepon.task_manager.entity.Subtask;
import com.izepon.task_manager.entity.Task;
import com.izepon.task_manager.enums.Status;
import com.izepon.task_manager.exception.ResourceNotFoundException;
import com.izepon.task_manager.repository.SubtaskRepository;
import com.izepon.task_manager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    public SubtaskService(SubtaskRepository subtaskRepository, TaskRepository taskRepository) {
        this.subtaskRepository = subtaskRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Subtask createSubtask(UUID taskId, SubtaskCreateRequest request) {
        Task parentTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Parent task not found with ID: " + taskId));

        Subtask newSubtask = new Subtask();
        newSubtask.setTitle(request.title());
        newSubtask.setDescription(request.description());
        newSubtask.setTask(parentTask);
        newSubtask.setStatus(Status.PENDING);

        return subtaskRepository.save(newSubtask);
    }

    @Transactional(readOnly = true)
    public List<Subtask> findSubtasksByTaskId(UUID taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Parent task not found with ID: " + taskId);
        }
        return subtaskRepository.findByTaskId(taskId);
    }

    @Transactional
    public Subtask updateSubtaskStatus(UUID subtaskId, Status newStatus) {
        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Subtask not found with ID: " + subtaskId));

        if (newStatus == Status.COMPLETED) {
            subtask.setConcludedAt(LocalDateTime.now());
        } else {
            subtask.setConcludedAt(null);
        }

        subtask.setStatus(newStatus);
        return subtask;
    }
}
