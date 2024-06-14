package br.com.evandro.todoList.repositories;

import br.com.evandro.todoList.domains.userattempts.UserAttemptsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAttemptsRepository extends JpaRepository<UserAttemptsEntity, UUID> {

    UserAttemptsEntity findByUserId(UUID userId);

}
