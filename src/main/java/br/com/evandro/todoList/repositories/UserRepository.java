package br.com.evandro.todoList.repositories;

import br.com.evandro.todoList.domains.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsernameIgnoringCaseOrEmailIgnoringCase(String username, String email);
    Optional<UserEntity> findByUsernameIgnoringCase(String username);
    Optional<UserEntity> findByEmailIgnoringCase(String email);
    boolean existsByUsername(String username);

}
