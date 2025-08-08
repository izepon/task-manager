package com.izepon.task_manager.repository;

import com.izepon.task_manager.entity.Task;
import com.izepon.task_manager.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {
    boolean existsBySubtasks_Task_IdAndSubtasks_StatusNot(UUID taskId, Status status);
}
