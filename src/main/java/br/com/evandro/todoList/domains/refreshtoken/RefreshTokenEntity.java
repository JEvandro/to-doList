package br.com.evandro.todoList.domains.refreshtoken;

import br.com.evandro.todoList.domains.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "expires_at", nullable = false)
    private Long expiresAt;

    @OneToOne
    @JoinColumn(name = "user_id", insertable=false, updatable=false)
    private UserEntity user;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    public RefreshTokenEntity(Long expiresAt, UUID userId){
        setExpiresAt(expiresAt);
        setUserId(userId);
    }

}
