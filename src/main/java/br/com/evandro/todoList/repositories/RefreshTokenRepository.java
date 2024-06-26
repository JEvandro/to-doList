package br.com.evandro.todoList.repositories;

import br.com.evandro.todoList.domains.refreshtoken.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);

}
