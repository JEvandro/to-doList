package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.task.exceptionsTask.TaskNotFoundException;
import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.domains.user.exceptionsUser.UpdatePasswordException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserFoundException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.dto.task.AllTasksResponseDTO;
import br.com.evandro.todoList.dto.user.*;
import br.com.evandro.todoList.providers.JWTProvider;
import br.com.evandro.todoList.repositories.TaskRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    JWTProvider jwtProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    public CreateUserResponseDTO executeCreate(CreateUserRequestDTO createUserRequestDTO){

        var user = this.userRepository.findByUsernameIgnoringCaseOrEmailIgnoringCase(
                createUserRequestDTO.username(),
                createUserRequestDTO.email());

        if(user.isPresent()) throw new UserFoundException("Usuário com este username e/ou email já existe");

        var password = passwordEncoder.encode(createUserRequestDTO.password());

        var userCreate =  this.userRepository.save(new UserEntity(
                createUserRequestDTO.name(),
                createUserRequestDTO.username(),
                createUserRequestDTO.email(),
                password)
        );

        var token = jwtProvider.generateToken(userCreate);

        return new CreateUserResponseDTO(userCreate.getName(),
                userCreate.getUsername(),
                userCreate.getEmail(),
                token
        );
    }

    public Record executeGet(String username, UUID userId){
        var user = this.userRepository.findByUsernameIgnoringCase(username).orElseThrow(() ->
            new UserNotFoundException("Usuário não existe")
        );

        if(!user.getId().equals(userId))
            return new GetOtherUserResponseDTO(user.getName(), user.getUsername(), user.getEmail(), user.getCreatedAt());

        return new GetUserResponseDTO(user.getId(),user.getName(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }

    public void executeDelete(UUID userId){
        this.userRepository.findById(userId).orElseThrow( () ->
           new UserNotFoundException("Não há usuário cadastrado com este id: " + userId)
        );

        taskRepository.findByUserId(userId).forEach( (task) -> {
            taskRepository.deleteById(task.getId());
        });

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

    public void executeUpdatePassword(UpdatePasswordRequestDTO updatePasswordRequestDTO, UUID userId) {
        var user = userRepository.findById(userId);

        if(!passwordEncoder.matches(updatePasswordRequestDTO.oldPassword(), user.get().getPassword()))
            throw new UpdatePasswordException("A antiga senha está errada!");

        if(updatePasswordRequestDTO.oldPassword().equals(updatePasswordRequestDTO.newPassword()))
            throw new UpdatePasswordException("A nova senha deve ser diferente da antiga!");

        if(!updatePasswordRequestDTO.newPassword().equals(updatePasswordRequestDTO.confirmPassword()))
            throw new UpdatePasswordException("A nova senha e a senha de confirmação devem ser iguais.");

        var password = passwordEncoder.encode(updatePasswordRequestDTO.newPassword());
        user.get().setPassword(password);
        userRepository.save(user.get());
    }
}
