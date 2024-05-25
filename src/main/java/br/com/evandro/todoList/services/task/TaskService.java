package br.com.evandro.todoList.services.task;

import br.com.evandro.todoList.domains.task.TaskEntity;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskAccessNotPermittedException;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskFoundException;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskNotFoundException;
import br.com.evandro.todoList.dto.task.*;
import br.com.evandro.todoList.repositories.TaskRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    public TaskResponseDTO executeCreate(TaskRequestDTO requestDTO, UUID userId){
        var verifyTask = this.taskRepository.findByDescriptionIgnoringCaseAndUserId(
                requestDTO.description(), userId);

        if(verifyTask.isPresent()) throw new TaskFoundException("Esta tarefa já está cadastrada para este usuário");

        var task = this.taskRepository.save(new TaskEntity(requestDTO.description(), userId));
        var user = this.userRepository.findById(userId).get();

        return new TaskResponseDTO(task.getDescription(), task.getCreatedAt(), user.getUsername());
    }

    public AllTasksResponseDTO executeGet(UUID taskId, UUID userId){
        var task = this.findTask(taskId);
        this.verifyAccessToTask(userId, task);

        return new AllTasksResponseDTO(
                task.getId(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt(),
                task.getUpdateAt());

    }

    public UpdateTaskResponseDTO executeUpdate(String description, UUID id, UUID userId){
        var task = this.findTask(id);
        this.verifyAccessToTask(userId, task);

        task.setDescription(description);
        task.setUpdateAt(LocalDateTime.now());
        this.taskRepository.save(task);

        return new UpdateTaskResponseDTO(task.getDescription(), task.getCreatedAt(), task.getUpdateAt());
    }

    public void executeDelete(UUID id, UUID userId){
        this.verifyAccessToTask(userId, this.findTask(id));
        this.taskRepository.deleteById(id);
    }

    public CompletedTaskResponseDTO executeUpdateCompleted(UUID id, UUID userId){
        var task = this.findTask(id);
        this.verifyAccessToTask(userId,task);

        task.setCompleted(true);
        taskRepository.save(task);

        return new CompletedTaskResponseDTO(task.getDescription(), task.isCompleted());
    }

    private TaskEntity findTask(UUID id){
        return taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Não há tarefa cadastrada com este id: " + id)
        );
    }

    private void verifyAccessToTask(UUID userId, TaskEntity task){
        if(!taskRepository.findByUserId(userId).contains(task)){
            throw new TaskAccessNotPermittedException("O id da tarefa passada não pertence a sua lista de tarefas");
        }
    }

}
