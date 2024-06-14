package br.com.evandro.todoList.repositories;

import br.com.evandro.todoList.domains.resetpassword.ResetPasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResetPasswordCodeRepository extends JpaRepository<ResetPasswordTokenEntity, UUID> {

    Optional<ResetPasswordTokenEntity> findByCode(String code);

}
