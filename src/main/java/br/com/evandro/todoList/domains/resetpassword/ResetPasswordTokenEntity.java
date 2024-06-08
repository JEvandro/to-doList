package br.com.evandro.todoList.domains.resetpassword;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
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

    private String token;
    private String email;
    private Long expiresAt;

    public ResetPasswordTokenEntity(String token, String email, Long expiresAt){
        setToken(token);
        setEmail(email);
        setExpiresAt(expiresAt);
    }

}
