package br.com.evandro.todoList.repositories;

import br.com.evandro.todoList.domains.resetpassword.ResetPasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordTokenEntity, UUID> {

    Optional<ResetPasswordTokenEntity> findByToken(String token);

}
