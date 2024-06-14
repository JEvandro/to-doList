package br.com.evandro.todoList.domains.userattempts;

import br.com.evandro.todoList.domains.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_attempts_login")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAttemptsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int countAttempts;

    private String previousStatus;

    @OneToOne
    @JoinColumn(name = "user_id", insertable = false, updatable=false)
    private UserEntity user;

    @Column(name = "user_id",nullable = false)
    private UUID userId;

    public UserAttemptsEntity(int countAttempts, UUID userId){
        setCountAttempts(countAttempts);
        setUserId(userId);
    }

    public void addAttempts(){
        this.countAttempts+=1;
    }

}
