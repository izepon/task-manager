package com.izepon.task_manager.util;

import com.izepon.task_manager.entity.Task;
import com.izepon.task_manager.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class TaskSpecification {

    public static Specification<Task> withFilters(Status status, UUID userId) {
        return Stream.of(
                        hasStatus(status),
                        hasUserId(userId)

                )
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse(null);
    }

    private static Specification<Task> hasStatus(Status status) {
        return status == null ? null : (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    private static Specification<Task> hasUserId(UUID userId) {
        return userId == null ? null : (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
    }
}
