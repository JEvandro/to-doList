package br.com.evandro.todoList.repositories;

import br.com.evandro.todoList.domains.resetpassword.ResetPasswordCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResetPasswordCodeRepository extends JpaRepository<ResetPasswordCodeEntity, UUID> {

    Optional<ResetPasswordCodeEntity> findByCode(String code);

}
