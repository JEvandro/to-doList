package br.com.evandro.todoList.controllers.task;

import br.com.evandro.todoList.domains.task.TaskEntity;
import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.dto.task.TaskRequestDTO;
import br.com.evandro.todoList.dto.task.UpdateTaskRequestDTO;
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

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TaskControllerTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private UserEntity user = new UserEntity(
            "TESTE",
            "TESTE",
            "TESTE@GAMIL.COM",
            "0123456789"
    );

    private UserEntity user1 = new UserEntity(
            "ANOTHER_TESTE",
            "ANOTHER_TESTE",
            "ANOTHER_TESTE@GMAIL.COM",
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
    @DisplayName("should not be create a task with same description for the same user")
    public void should_not_be_create_a_task_with_same_description() throws Exception {
        user = userRepository.saveAndFlush(user);
        var task = taskRepository.saveAndFlush(new TaskEntity("TASK_TESTE",user.getId()));

        mvc.perform(
                MockMvcRequestBuilders.post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.ObjectToJSON(new TaskRequestDTO("TASK_TESTE")))
                .requestAttr("userId", user.getId())
                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isConflict()
        );
    }

    @Test
    @DisplayName("should not be create a task with no content")
    public void should_not_be_create_a_task_with_no_content() throws Exception {
        user = userRepository.saveAndFlush(user);

        mvc.perform(
                MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.ObjectToJSON(new TaskRequestDTO("  ")))
                        .requestAttr("userId", user.getId())
                        .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable()
        );
    }

    @Test
    @DisplayName("should not be create a task without the argument")
    public void should_not_be_create_a_task_without_the_argument() throws Exception {
        user = userRepository.saveAndFlush(user);

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.ObjectToJSON(null))
                                .requestAttr("userId", user.getId())
                                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()
                );
    }

    @Test
    @DisplayName("should not be able get information of the task if not exist")
    public void should_not_be_able_get_information_of_the_task_if_not_exist() throws Exception {
        user = userRepository.saveAndFlush(user);
        taskRepository.saveAndFlush(new TaskEntity("TESTE", user.getId()));

        mvc.perform(
                        MockMvcRequestBuilders.get("/api/tasks/{taskId}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .requestAttr("userId", user.getId())
                                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isNotFound()
                );
    }

    @Test
    @DisplayName("should not be able get information of the task of another user")
    public void should_not_be_able_get_information_of_the_task_of_another_user() throws Exception {
        user = userRepository.saveAndFlush(user);
        var task = taskRepository.saveAndFlush(new TaskEntity("TASK_ANOTHER_TESTE", user1.getId()));

        mvc.perform(
                        MockMvcRequestBuilders.get("/api/tasks/{taskId}", task.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .requestAttr("userId", user.getId())
                                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()
                );
    }

    @Test
    @DisplayName("should not be able update a task if task not exist")
    public void should_not_be_able_update_a_task_if_task_not_exist() throws Exception {
        user = userRepository.saveAndFlush(user);
        taskRepository.saveAndFlush(new TaskEntity("TESTE", user.getId()));

        mvc.perform(
                        MockMvcRequestBuilders.patch("/api/tasks/{taskId}", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.ObjectToJSON(new UpdateTaskRequestDTO("UPDATE_TESTE")))
                                .requestAttr("userId", user.getId())
                                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isNotFound()
                );
    }

    @Test
    @DisplayName("should not be able update a task if no content")
    public void should_not_be_able_update_a_task_if_no_content() throws Exception {
        user = userRepository.saveAndFlush(user);
        var task = taskRepository.saveAndFlush(new TaskEntity("TESTE", user.getId()));

        mvc.perform(
                        MockMvcRequestBuilders.patch("/api/tasks/{taskId}", task.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.ObjectToJSON(new UpdateTaskRequestDTO(" ")))
                                .requestAttr("userId", user.getId())
                                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable()
                );
    }

    @Test
    @DisplayName("should not be able update task without the argument")
    public void should_not_be_able_update_task_without_the_argument() throws Exception {
        user = userRepository.saveAndFlush(user);
        var task = taskRepository.saveAndFlush(new TaskEntity("TESTE", user.getId()));

        mvc.perform(
                        MockMvcRequestBuilders.patch("/api/tasks/{taskId}", task.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.ObjectToJSON(null))
                                .requestAttr("userId", user.getId())
                                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()
                );
    }

    @Test
    @DisplayName("should not be able update task of another user")
    public void should_not_be_able_update_task_of_another_user() throws Exception {
        user = userRepository.saveAndFlush(user);
        user1 = userRepository.saveAndFlush(user1);
        var task = taskRepository.saveAndFlush(new TaskEntity("TASK_ANOTHER_TESTE", user1.getId()));

        mvc.perform(
                        MockMvcRequestBuilders.get("/api/tasks/{taskId}", task.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.ObjectToJSON(new UpdateTaskRequestDTO("TESTE")))
                                .requestAttr("userId", user.getId())
                                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()
                );
    }

    @Test
    @DisplayName("should not be able update task to complete if task not exist")
    public void should_not_be_able_update_a_task_to_complete_if_task_not_exist() throws Exception {
        user = userRepository.saveAndFlush(user);
        var task = taskRepository.saveAndFlush(new TaskEntity("TASK_TESTE", user.getId()));

        mvc.perform(
                        MockMvcRequestBuilders.patch("/api/tasks/{taskId}/complete", UUID.randomUUID())
                                .contentType(MediaType.APPLICATION_JSON)
                                .requestAttr("userId", user.getId())
                                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isNotFound()
                );
    }

    @Test
    @DisplayName("should not be able update task to complete of another user")
    public void should_not_be_able_update_task_to_complete_of_another_user() throws Exception {
        user = userRepository.saveAndFlush(user);
        user1 = userRepository.saveAndFlush(user1);
        var task = taskRepository.saveAndFlush(new TaskEntity("TASK_ANOTHER_TESTE", user1.getId()));

        mvc.perform(
                        MockMvcRequestBuilders.patch("/api/tasks/{taskId}/complete", task.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .requestAttr("userId", user.getId())
                                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()
                );
    }

    @Test
    @DisplayName("should not be able delete a task of another user")
    public void should_not_be_able_delete_a_task_of_another_user() throws Exception {
        user = userRepository.saveAndFlush(user);
        user1 = userRepository.saveAndFlush(user1);
        var task = taskRepository.saveAndFlush(new TaskEntity("TASK_ANOTHER_TESTE", user1.getId()));

        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/tasks/{taskId}", task.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .requestAttr("userId", user.getId())
                                .header("Authorization", TestUtils.generateToken(user.getId(), "USER_123@#")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()
                );

    }


}
