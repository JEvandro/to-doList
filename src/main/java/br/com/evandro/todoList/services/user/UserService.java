package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.task.exceptionsTask.TaskNotFoundException;
import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserFoundException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.dto.task.AllTasksResponseDTO;
import br.com.evandro.todoList.dto.user.UserRequestDTO;
import br.com.evandro.todoList.dto.user.UserResponseDTO;
import br.com.evandro.todoList.repositories.TaskRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    public UserEntity executeCreate(UserRequestDTO userRequestDTO){

        var user = this.userRepository.findByUsernameIgnoringCaseOrEmailIgnoringCase(
                userRequestDTO.username(),
                userRequestDTO.email());

        if(user.isPresent()) throw new UserFoundException("Usuário com este username e/ou email já existe");

        return this.userRepository.save(new UserEntity(
                userRequestDTO.name(),
                userRequestDTO.username(),
                userRequestDTO.email(),
                userRequestDTO.password())
        );
    }

    public UserResponseDTO executeGet(String username){
        var user = this.userRepository.findByUsernameIgnoringCase(username).orElseThrow(() ->
            new UserNotFoundException("Usuário não existe")
        );

        return new UserResponseDTO(user.getName(), user.getUsername(), user.getEmail(), user.getPassword());
    }

    public void executeDelete(UUID userId){
        this.userRepository.findById(userId).orElseThrow( () ->
           new UserNotFoundException("Não há usuário cadastrado com este id: " + userId)
        );

        System.out.println("Passei pelo exceção notFound");

        taskRepository.findByUserId(userId).forEach( (task) -> {
            taskRepository.deleteById(task.getId());
        });

        System.out.println("Passei pela comando de exclusão da lista de task do usuario");
        this.userRepository.deleteById(userId);
    }

    public List<AllTasksResponseDTO> executeGetAllTasks(UUID userId){
        var listTask = this.taskRepository.findByUserId(userId);

        if(listTask.isEmpty()) throw new TaskNotFoundException("Não existe tarefa(s) criada(s) para esse usuário");

        List< AllTasksResponseDTO > listAllTasks = new ArrayList<>();
        listTask.forEach( taskEntity -> {
            listAllTasks.add(new AllTasksResponseDTO(
                    taskEntity.getId(),
                    taskEntity.getDescription(),
                    taskEntity.isCompleted(),
                    taskEntity.getCreatedAt(),
                    taskEntity.getUpdateAt()
            ));
        });

        return listAllTasks;
    }

}
