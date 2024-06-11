package br.com.evandro.todoList.repositories;

import br.com.evandro.todoList.domains.codeconfirmation.CodeConfirmationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CodeConfirmationRepository extends JpaRepository<CodeConfirmationEntity, UUID> {

    Optional<CodeConfirmationEntity> findByCodeAndUserId(String code, UUID userId);

}
