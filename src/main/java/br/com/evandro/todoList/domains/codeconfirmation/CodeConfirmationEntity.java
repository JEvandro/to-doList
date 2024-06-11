package br.com.evandro.todoList.domains.codeconfirmation;

import br.com.evandro.todoList.domains.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "code_confirmation")
public class CodeConfirmationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String code;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "code_expires_at", nullable = false)
    private Long codeExpiresAt;

    public CodeConfirmationEntity(String code, UUID userId, Long codeExpiresAt){
        setCode(code);
        setUserId(userId);
        setCodeExpiresAt(codeExpiresAt);
    }

}
