package br.com.evandro.todoList.services.task;

import br.com.evandro.todoList.domains.task.TaskEntity;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskAccessNotPermittedException;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskFoundException;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskNotFoundException;
import br.com.evandro.todoList.dto.task.TaskRequestDTO;
import br.com.evandro.todoList.repositories.TaskRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("should not be able create a task with same description in the same user")
    public void should_not_be_able_create_a_task_with_same_description_in_the_same_user(){
        var userId = UUID.randomUUID();
        var task = new TaskRequestDTO("teste");

        var taskAlreadyExist = new TaskEntity();
        taskAlreadyExist.setDescription(task.description());
        taskAlreadyExist.setUserId(userId);

        when(taskRepository.findByDescriptionIgnoringCaseAndUserId(task.description(), userId)).thenReturn(Optional.of(taskAlreadyExist));

        try {
            taskService.executeCreate(task, userId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskFoundException.class);
        }
    }

    @Test
    @DisplayName("should not be able get a task if task with id not exist")
    public void should_not_be_able_get_a_task_if_id_is_wrong(){
        try {
            taskService.executeGet(null, null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskNotFoundException.class);
        }
    }

    @Test
    @DisplayName("should not be able get a task if the user not has access")
    public void should_not_be_able_get_a_task_if_the_user_not_has_access(){
        var userId = UUID.randomUUID();
        var otherTaskId = UUID.randomUUID();
        var otherUserId = UUID.randomUUID();

        testTaskNotPermittedException(userId, otherUserId, otherTaskId);

        try {
            taskService.executeGet(otherTaskId, userId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskAccessNotPermittedException.class);
        }
    }

    @Test
    @DisplayName("should not be able update a task if task with id not exist")
    public void should_not_be_able_update_a_task_if_task_with_id_not_exist(){
        try {
            taskService.executeUpdate("teste", null, null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskNotFoundException.class);
        }
    }

    @Test
    @DisplayName("should not be able update a task if the user not has access")
    public void should_not_be_able_update_a_task_if_the_user_not_has_access(){
        var userId = UUID.randomUUID();
        var otherTaskId = UUID.randomUUID();
        var otherUserId = UUID.randomUUID();

        testTaskNotPermittedException(userId, otherUserId, otherTaskId);

        try {
            taskService.executeUpdate("teste", otherTaskId, userId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskAccessNotPermittedException.class);
        }
    }

    @Test
    @DisplayName("should not be able delete a task if the user not has access")
    public void should_not_be_able_delete_a_task_if_the_user_not_has_access(){
        var userId = UUID.randomUUID();
        var otherTaskId = UUID.randomUUID();
        var otherUserId = UUID.randomUUID();

        testTaskNotPermittedException(userId, otherUserId, otherTaskId);

        try {
            taskService.executeDelete(otherTaskId, userId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskAccessNotPermittedException.class);
        }
    }

    @Test
    @DisplayName("should not be able update task to complete if task with id not exist")
    public void should_not_be_able_update_task_to_complete_if_task_with_id_not_exist(){
        try {
            taskService.executeUpdateCompleted(null, null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskNotFoundException.class);
        }
    }

    @Test
    @DisplayName("should not be able update task to complete if the user not has access")
    public void should_not_be_able_update_a_task_to_complete_if_the_user_not_has_access(){
        var userId = UUID.randomUUID();
        var otherTaskId = UUID.randomUUID();
        var otherUserId = UUID.randomUUID();

        testTaskNotPermittedException(userId, otherUserId, otherTaskId);

        try {
            taskService.executeUpdateCompleted(otherTaskId, userId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskAccessNotPermittedException.class);
        }
    }

    private void testTaskNotPermittedException(UUID userId, UUID otherUserId, UUID otherTaskId){
        var taskOtherUser = new TaskEntity();
        taskOtherUser.setUserId(otherUserId);

        List<TaskEntity> taskEntityList = new ArrayList<>();
        var taskUser = new TaskEntity();
        taskUser.setUserId(userId);
        taskEntityList.add(taskUser);

        when(taskRepository.findById(otherTaskId)).thenReturn(Optional.of(taskOtherUser));
        when(taskRepository.findByUserId(userId)).thenReturn(taskEntityList);
    }

}
