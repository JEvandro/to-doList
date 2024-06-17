package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.task.exceptionsTask.TaskNotFoundException;
import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.domains.user.UserStatusEnum;
import br.com.evandro.todoList.domains.user.exceptionsUser.UpdatePasswordException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserFoundException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.domains.userattempts.UserAttemptsEntity;
import br.com.evandro.todoList.dto.task.AllTasksResponseDTO;
import br.com.evandro.todoList.dto.user.request.CreateUserRequestDTO;
import br.com.evandro.todoList.dto.user.request.UpdatePasswordRequestDTO;
import br.com.evandro.todoList.dto.user.request.UpdateProfileUserRequestDTO;
import br.com.evandro.todoList.dto.user.response.CreateUserResponseDTO;
import br.com.evandro.todoList.dto.user.response.GetOtherUserResponseDTO;
import br.com.evandro.todoList.dto.user.response.GetUserResponseDTO;
import br.com.evandro.todoList.dto.user.response.UpdateProfileUserResponseDTO;
import br.com.evandro.todoList.providers.JWTProviderRefreshToken;
import br.com.evandro.todoList.providers.JWTProviderToken;
import br.com.evandro.todoList.repositories.TaskRepository;
import br.com.evandro.todoList.repositories.UserAttemptsRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserAttemptsRepository userAttemptsRepository;

    @Autowired
    private JWTProviderToken jwtProviderToken;

    @Autowired
    private JWTProviderRefreshToken jwtProviderRefreshToken;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                password,
                UserStatusEnum.PENDENT)
        );

        userAttemptsRepository.save(new UserAttemptsEntity(0, userCreate.getId()));
        var token = jwtProviderToken.generateToken(userCreate);
        var refreshToken = jwtProviderRefreshToken.generateRefreshToken(userCreate.getId());
        emailService.sendMailToUserConfirmation(userCreate.getId());

        return new CreateUserResponseDTO(userCreate.getName(),
                userCreate.getUsername(),
                userCreate.getEmail(),
                userCreate.getUserStatusEnum().getDescription(),
                token,
                refreshToken.getId()
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
        findUser(userId);

        jwtProviderRefreshToken.deleteByUserId(userId);

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
        var user = findUser(userId);

        if(!passwordEncoder.matches(updatePasswordRequestDTO.oldPassword(), user.getPassword()))
            throw new UpdatePasswordException("A antiga senha está errada!");

        if(updatePasswordRequestDTO.oldPassword().equals(updatePasswordRequestDTO.newPassword()))
            throw new UpdatePasswordException("A nova senha deve ser diferente da antiga!");

        if(!updatePasswordRequestDTO.newPassword().equals(updatePasswordRequestDTO.confirmPassword()))
            throw new UpdatePasswordException("A nova senha e a senha de confirmação devem ser iguais.");

        var password = passwordEncoder.encode(updatePasswordRequestDTO.newPassword());
        user.setPassword(password);
        userRepository.save(user);
    }

    public UpdateProfileUserResponseDTO executeUpdateProfile(UpdateProfileUserRequestDTO updateProfileUserRequestDTO, UUID userId) {
        var user = findUser(userId);
        boolean x = false;

        if(!updateProfileUserRequestDTO.name().isEmpty()) {
            user.setName(updateProfileUserRequestDTO.name());
            x = true;
        }

        if(!updateProfileUserRequestDTO.username().isEmpty()) {
            var index = updateProfileUserRequestDTO.username().indexOf(" ");
            var username = updateProfileUserRequestDTO.username().substring(0,index);
            user.setUsername(username);
            x = true;
        }

        if(!updateProfileUserRequestDTO.email().isEmpty()) {
            user.setEmail(updateProfileUserRequestDTO.email());
            x = true;
        }

        if(x) {user = userRepository.save(user);}

        return new UpdateProfileUserResponseDTO(
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdateAt()
        );
    }

    private UserEntity findUser(UUID userId){
        return userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("Não exista usuário cadastrado com este id: " + userId)
        );
    }
}
