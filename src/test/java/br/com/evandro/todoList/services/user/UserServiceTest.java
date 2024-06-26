package br.com.evandro.todoList.services.user;

import br.com.evandro.todoList.domains.refreshtoken.RefreshTokenEntity;
import br.com.evandro.todoList.domains.task.TaskEntity;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskNotFoundException;
import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.domains.user.UserStatusEnum;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserFoundException;
import br.com.evandro.todoList.domains.user.exceptionsUser.UserNotFoundException;
import br.com.evandro.todoList.domains.userattempts.UserAttemptsEntity;
import br.com.evandro.todoList.dto.task.AllTasksResponseDTO;
import br.com.evandro.todoList.dto.user.request.CreateUserRequestDTO;
import br.com.evandro.todoList.dto.user.response.CreateUserResponseDTO;
import br.com.evandro.todoList.dto.user.response.GetOtherUserResponseDTO;
import br.com.evandro.todoList.dto.user.response.GetUserResponseDTO;
import br.com.evandro.todoList.providers.JWTProviderRefreshToken;
import br.com.evandro.todoList.providers.JWTProviderToken;
import br.com.evandro.todoList.repositories.TaskRepository;
import br.com.evandro.todoList.repositories.UserAttemptsRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import jakarta.validation.constraints.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserAttemptsRepository userAttemptsRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTProviderToken jwtProviderToken;

    @Mock
    private JWTProviderRefreshToken jwtProviderRefreshToken;

    @Mock
    private EmailService emailService;

    private UUID userId = UUID.randomUUID();

    @Test
    @DisplayName("should not be able create an user with user already exist")
    public void should_not_be_able_create_an_user_with_user_already_exist(){
        var username = "teste1";
        var email = "teste1@gmail.com";

        when(userRepository.findByUsernameIgnoringCaseOrEmailIgnoringCase(username,email)).thenReturn(Optional.of(new UserEntity()));

        try {
            userService.executeCreate(new CreateUserRequestDTO(null, username,email, null));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(UserFoundException.class);
        }
    }

    @Test
    @DisplayName("should be able create an user")
    public void should_be_able_create_an_user(){
        var createUser = new CreateUserRequestDTO("teste",
                "teste",
                "teste@gmail.com",
                "0123456789"
        );

        var userEntity = new UserEntity(createUser.name(),
                createUser.username(),
                createUser.email(),
                passwordEncoder.encode(createUser.password()),
                UserStatusEnum.PENDENT
        );

        var userAlreadyCreated = new UserEntity();
        userAlreadyCreated.setName(createUser.name());
        userAlreadyCreated.setUsername(createUser.username());
        userAlreadyCreated.setId(userId);
        userAlreadyCreated.setUserStatus(UserStatusEnum.PENDENT);

        var userAttempts = new UserAttemptsEntity(0, userId);

        when(userRepository.findByUsernameIgnoringCaseOrEmailIgnoringCase(
                createUser.username(),
                createUser.email())).
                thenReturn(Optional.empty());

        when(userRepository.save(userEntity)).thenReturn(userAlreadyCreated);
        when(userAttemptsRepository.save(userAttempts)).thenReturn(new UserAttemptsEntity());
        when(jwtProviderToken.generateToken(userAlreadyCreated)).thenReturn("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        when(jwtProviderRefreshToken.generateRefreshToken(userAlreadyCreated.getId())).thenReturn(new RefreshTokenEntity(Instant.now().plusMillis(6000).toEpochMilli(), UUID.randomUUID()));
        doNothing().when(emailService).sendMailToUserConfirmation(userId);

        var result = userService.executeCreate(createUser);

        assertThat(result).isInstanceOf(CreateUserResponseDTO.class);
        assertThat(result).hasFieldOrProperty("username");
        assertNotNull(result.username());

    }

    @Test
    @DisplayName("should not be able get information user if user not exist")
    public void should_not_be_able_get_information_user_if_user_not_exist(){
        try {
            userService.executeGet(null, null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(UserNotFoundException.class);
        }
    }

    @Test
    @DisplayName("should be able get information user")
    public void should_be_able_get_information_user() throws NoSuchFieldException {
        var username = "teste";

        var userInformation = new UserEntity();
        userInformation.setId(userId);
        userInformation.setUserStatus(UserStatusEnum.PENDENT);

        when(userRepository.findByUsernameIgnoringCase(username)).thenReturn(Optional.of(userInformation));

        var result = userService.executeGet(username, userId);

        assertThat(result).isInstanceOf(GetUserResponseDTO.class);
        assertThat(result).hasFieldOrProperty("id");
        assertNotNull(result.getClass().getDeclaredField("id"));

    }

    @Test
    @DisplayName("should be able get information another user")
    public void should_be_able_get_information_another_user() throws NoSuchFieldException {
        var anotherUser = new UserEntity(UUID.randomUUID(),"", "teste1", "", "", "", null, LocalDateTime.now(), LocalDateTime.now());
        var username = "teste1";

        when(userRepository.findByUsernameIgnoringCase(username)).thenReturn(Optional.of(anotherUser));

        var result = userService.executeGet(username, userId);

        assertThat(result).isInstanceOf(GetOtherUserResponseDTO.class);
        assertThat(result).hasOnlyFields("name", "username", "email", "createdAt");
        assertNotNull(result.getClass().getDeclaredField("createdAt"));
    }

    @Test
    @DisplayName("should not be able delete an user that is already delete")
    public void should_not_be_able_delete_an_user_that_is_already_delete(){
        try {
            userService.executeDelete(null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(UserNotFoundException.class);
        }
    }

    @Test
    @DisplayName("should not be able get all tasks of the user if not exist task")
    public void should_not_be_able_get_all_tasks_of_the_user_if_not_exist_task(){
        when(taskRepository.findByUserId(userId)).thenReturn(List.of(new TaskEntity()));

        try {
            userService.executeGetAllTasks(userId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskNotFoundException.class);
        }
    }

    @Test
    @DisplayName("should be able get all tasks of the user")
    public void should_be_able_get_all_tasks_of_the_user(){
        List<TaskEntity> taskEntityList = new ArrayList<>();
        for(int i=0; i<3; i++){
            var id = UUID.randomUUID();
            var task = new TaskEntity("teste", userId);
            task.setId(id);
            taskEntityList.add(task);
        }

        when(taskRepository.findByUserId(userId)).thenReturn(taskEntityList);

        var result = userService.executeGetAllTasks(userId);

        assertNotNull(result);
        assertThat(result).hasOnlyElementsOfType(AllTasksResponseDTO.class);
        for(int i=0; i<3; i++) {
            assertThat(result).element(i).hasFieldOrProperty("id");
            assertNotNull(result.get(i).id());
        }
    }

}
