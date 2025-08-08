package com.izepon.task_manager.repository;

import com.izepon.task_manager.entity.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, UUID> {
    List<Subtask> findByTaskId(UUID taskId);
}
