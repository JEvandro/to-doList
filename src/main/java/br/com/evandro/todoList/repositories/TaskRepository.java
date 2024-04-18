package br.com.evandro.todoList.repositories;

import br.com.evandro.todoList.domains.task.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    List<TaskEntity> findByUserId(UUID userId);

    Optional<TaskEntity> findByDescriptionIgnoringCaseAndUserId(String description, UUID userID);

}
