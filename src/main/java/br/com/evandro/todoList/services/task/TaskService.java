package br.com.evandro.todoList.services.task;

import br.com.evandro.todoList.domains.task.TaskEntity;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskFoundException;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskNotFoundException;
import br.com.evandro.todoList.dto.task.CompletedTaskResponseDTO;
import br.com.evandro.todoList.dto.task.TaskRequestDTO;
import br.com.evandro.todoList.dto.task.TaskResponseDTO;
import br.com.evandro.todoList.dto.task.UpdateTaskResponseDTO;
import br.com.evandro.todoList.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    public TaskResponseDTO executeCreate(TaskRequestDTO requestDTO){
        var verifyTask = this.taskRepository.findByDescriptionIgnoringCaseAndUserId(
                requestDTO.description(),
                requestDTO.userId());

        if(verifyTask.isPresent()) throw new TaskFoundException("Esta tarefa já está cadastrada para este usuário");

        var task = taskRepository.save(new TaskEntity(requestDTO.description(), requestDTO.userId()));
        return new TaskResponseDTO(task.getDescription(), task.getCreatedAt(), task.getUserId());
    }


    public UpdateTaskResponseDTO executeUpdate(String description, UUID id){
        var task = this.findTask(id);

        task.setDescription(description);
        task.setUpdateAt(LocalDateTime.now());
        this.taskRepository.save(task);

        return new UpdateTaskResponseDTO(task.getDescription(), task.getCreatedAt(), task.getUpdateAt());
    }

    public void executeDelete(UUID id){
        this.findTask(id);
        this.taskRepository.deleteById(id);
    }

    public CompletedTaskResponseDTO executeUpdateCompleted(UUID id){
        var task = this.findTask(id);
        task.setCompleted(true);
        taskRepository.save(task);

        return new CompletedTaskResponseDTO(task.getDescription(), task.isCompleted());
    }

    private TaskEntity findTask(UUID id){
        return taskRepository.findById(id).orElseThrow(() ->
                new TaskNotFoundException("Não há tarefa cadastrada com este id: " + id)
        );
    }

}
