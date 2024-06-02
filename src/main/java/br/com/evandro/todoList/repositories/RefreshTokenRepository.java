package br.com.evandro.todoList.repositories;

import br.com.evandro.todoList.domains.refreshtoken.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    RefreshTokenEntity findByUserId(UUID userId);

}
