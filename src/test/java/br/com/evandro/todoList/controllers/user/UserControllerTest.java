package br.com.evandro.todoList.controllers.user;

import br.com.evandro.todoList.domains.task.TaskEntity;
import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.dto.user.CreateUserRequestDTO;
import br.com.evandro.todoList.repositories.TaskRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import br.com.evandro.todoList.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private UserEntity user = new UserEntity(
            "TESTE",
            "TESTE",
            "TESTE@GMAIL.COM",
            "0123456789"
    );

    private CreateUserRequestDTO request = new CreateUserRequestDTO(
            "TESTE",
            "TESTE",
            "TESTE@GMAIL.COM",
            "0123456789"
    );

    @Before
    public void setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @DisplayName("should not be able create a user with params wrong")
    public void should_not_be_able_create_a_user_with_params_wrong() throws Exception {
        var request = new CreateUserRequestDTO(
                "TESTE",
                "TE STE",
                "TESTEGMAIL.COM",
                "012345"
        );

        mvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.ObjectToJSON(request)))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable()
        );
    }

    @Test
    @DisplayName("should not be able create a user if already exist")
    public void should_not_be_able_create_a_user_if_already_exist() throws Exception {
        userRepository.saveAndFlush(user);

        mvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.ObjectToJSON(request)))
                .andExpect(MockMvcResultMatchers.status().isConflict()
        );

    }

    @Test
    @DisplayName("should be able create a new user")
    public void should_be_able_create_a_new_user() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.ObjectToJSON(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    @DisplayName("should not be able get information user if user not exist")
    public void should_not_be_able_get_information_user_if_user_not_exist() throws Exception {
        String username = "TESTE1";
        var user = userRepository.saveAndFlush(this.user);

        mvc.perform(
                MockMvcRequestBuilders.get("/api/users/{username}", username)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @DisplayName("should be able get information user")
    public void should_be_able_get_information_user() throws Exception {
        user = userRepository.saveAndFlush(user);

        mvc.perform(
                MockMvcRequestBuilders.get("/api/users/{username}", user.getUsername())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    @DisplayName("should not be able delete an user if he is already delete")
    public void should_not_be_able_delete_an_user_if_he_is_already_delete() throws Exception {
        userRepository.saveAndFlush(user);
        userRepository.delete(user);

        mvc.perform(
                MockMvcRequestBuilders.delete("/api/users/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", user.getId())
                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @DisplayName("should be able delete an user")
    public void should_be_able_delete_an_user() throws Exception {
        userRepository.saveAndFlush(user);

        mvc.perform(
                MockMvcRequestBuilders.delete("/api/users/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", user.getId())
                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    @DisplayName("should not be able get list of tasks of the user if not exist task create")
    public void should_not_be_able_get_list_of_tasks_of_the_user_if_not_exist_task_create() throws Exception {
        userRepository.saveAndFlush(user);
     // taskRepository.saveAndFlush(new TaskEntity("TASK_TESTE", user.getId()));

        mvc.perform(
                MockMvcRequestBuilders.get("/api/users/mytasks")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", user.getId())
                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @DisplayName("should be able get list of tasks of the user")
    public void should_be_able_get_list_of_tasks_of_the_user() throws Exception {
        userRepository.saveAndFlush(user);
        taskRepository.saveAndFlush(new TaskEntity("TASK_TESTE", user.getId()));

        mvc.perform(
                MockMvcRequestBuilders.get("/api/users/mytasks")
                .contentType(MediaType.APPLICATION_JSON)
                .requestAttr("userId", user.getId())
                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isOk()
        );
    }

}
