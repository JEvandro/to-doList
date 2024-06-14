package br.com.evandro.todoList.domains.resetpassword;

import br.com.evandro.todoList.domains.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "reset_password_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String code;
    private String email;
    private Long expiresAt;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    public ResetPasswordTokenEntity(String code, String email, Long expiresAt, UUID userId){
        setCode(code);
        setEmail(email);
        setExpiresAt(expiresAt);
        setUserId(userId);
    }

}
