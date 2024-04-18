package br.com.evandro.todoList.domains.task;

import br.com.evandro.todoList.domains.user.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_completed")
    private boolean isCompleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable=false, updatable=false)
    private UserEntity user;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    public TaskEntity(String description, UUID userId){
        this.description = description;
        this.userId = userId;
        this.isCompleted = false;
    }

}
