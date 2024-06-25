package br.com.evandro.todoList.domains.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(example = "738dc9da-d507-4c38-b067-fb0072c00ea7")
    private UUID id;

    @Column(name = "name", nullable = false)
    @Schema(example = "Admin")
    private String name;

    @Column(name = "username", nullable = false)
    @Schema(example = "admin")
    private String username;

    @Column(name = "email", nullable = false)
    @Schema(example = "admin@gmail.com")
    private String email;

    @Column(name = "password", nullable = false)
    @Schema(example = "$2a$10$mn9/6lz3jt3xgq2w3JTKeePrsAyN4MgyDoF6w/75S4NOxqh3lUaHO")
    private String password;

    @Column(name = "user_status")
    private String userStatus;

    @Column(name = "blocked_expires_at")
    private Long blockedExpiresAt;

    @CreationTimestamp
    @Schema(example = "2024-04-22T16:59:10.811838")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updateAt;

    public UserStatusEnum getUserStatusEnum(){
        return UserStatusEnum.valueOfStatus(userStatus);
    }

    public void setUserStatus(UserStatusEnum userStatus){
        if(userStatus != null)
            this.userStatus = userStatus.getStatus();
    }

    public UserEntity(String name, String username, String email, String password, UserStatusEnum userStatus){
        setName(name);
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setUserStatus(userStatus);
    }

}
