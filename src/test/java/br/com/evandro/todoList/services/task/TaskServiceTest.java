package br.com.evandro.todoList.services.task;

import br.com.evandro.todoList.domains.task.TaskEntity;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskAccessNotPermittedException;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskFoundException;
import br.com.evandro.todoList.domains.task.exceptionsTask.TaskNotFoundException;
import br.com.evandro.todoList.domains.user.UserEntity;
import br.com.evandro.todoList.dto.task.*;
import br.com.evandro.todoList.repositories.TaskRepository;
import br.com.evandro.todoList.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private UUID userId = UUID.randomUUID();
    private UUID taskId = UUID.randomUUID();

    @Test
    @DisplayName("should not be able create a task with same description in the same user")
    public void should_not_be_able_create_a_task_with_same_description_in_the_same_user(){
        var task = new TaskRequestDTO("teste");

        when(taskRepository.findByDescriptionIgnoringCaseAndUserId(task.description(), userId)).thenReturn(Optional.of(new TaskEntity()));

        try {
            taskService.executeCreate(task, userId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskFoundException.class);
        }
    }

    @Test
    @DisplayName("should be able create a task")
    public void should_be_able_create_a_task(){

        var taskCreate = new TaskEntity();
        taskCreate.setCreatedAt(LocalDateTime.now());

        when(taskRepository.save(new TaskEntity(null, userId))).thenReturn(taskCreate);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new UserEntity()));

        var result = taskService.executeCreate(new TaskRequestDTO(null), userId);

        assertThat(result).isInstanceOf(TaskResponseDTO.class);
        assertThat(result).hasFieldOrProperty("createdAt");
        assertNotNull(result.createdAt());
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
        var otherUserId = UUID.randomUUID();

        testTaskNotPermittedException(otherUserId);

        try {
            taskService.executeGet(taskId, userId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskAccessNotPermittedException.class);
        }
    }

    @Test
    @DisplayName("should be able get a task")
    public void should_be_able_get_a_task(){
        testTaskHasPermittedAccess();

        var result = taskService.executeGet(taskId, userId);

        assertThat(result).isInstanceOf(AllTasksResponseDTO.class);
        assertThat(result).hasFieldOrProperty("id");
        assertNotNull(result.id());
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
        var otherUserId = UUID.randomUUID();

        testTaskNotPermittedException(otherUserId);

        try {
            taskService.executeUpdate("teste", taskId, userId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskAccessNotPermittedException.class);
        }
    }

    @Test
    @DisplayName("shoud be able update a task")
    public void should_be_able_update_a_task(){
        var task = new TaskEntity("", userId);
        task.setId(taskId);
        task.setCreatedAt(LocalDateTime.now());

        List<TaskEntity> taskEntityList = new ArrayList<>();
        taskEntityList.add(task);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.findByUserId(userId)).thenReturn(taskEntityList);

        var result = taskService.executeUpdate(null, taskId, userId);

        assertThat(result).isInstanceOf(UpdateTaskResponseDTO.class);
        assertThat(result).hasFieldOrProperty("updateAt");
        assertNotNull(result.updateAt());
        assertThat(result).hasFieldOrProperty("createdAt");
        assertNotNull(result.createdAt());
        assertThat(result.createdAt()).isNotEqualTo(result.updateAt());
    }

    @Test
    @DisplayName("should not be able delete a task if the user not has access")
    public void should_not_be_able_delete_a_task_if_the_user_not_has_access(){
        var otherUserId = UUID.randomUUID();

        testTaskNotPermittedException(otherUserId);

        try {
            taskService.executeDelete(taskId, userId);
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
        var otherUserId = UUID.randomUUID();

        testTaskNotPermittedException(otherUserId);

        try {
            taskService.executeUpdateCompleted(taskId, userId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(TaskAccessNotPermittedException.class);
        }
    }

    @Test
    @DisplayName("should be able update a task to completed")
    public void should_be_able_update_a_task_to_complete(){
        testTaskHasPermittedAccess();
        var result = taskService.executeUpdateCompleted(taskId, userId);

        assertThat(result).isInstanceOf(CompletedTaskResponseDTO.class);
        assertThat(result).hasFieldOrProperty("isCompleted");
        assertNotNull(result.isCompleted());
        assertThat(result).isNotEqualTo(false);
    }

    private void testTaskNotPermittedException(UUID otherUserId){
        var taskOtherUser = new TaskEntity();
        taskOtherUser.setUserId(otherUserId);

        List<TaskEntity> taskEntityList = new ArrayList<>();
        var taskUser = new TaskEntity();
        taskUser.setUserId(userId);
        taskEntityList.add(taskUser);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskOtherUser));
        when(taskRepository.findByUserId(userId)).thenReturn(taskEntityList);
    }

    private void testTaskHasPermittedAccess(){
        var task = new TaskEntity("", userId);
        task.setId(taskId);

        List<TaskEntity> taskEntityList = new ArrayList<>();
        taskEntityList.add(task);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.findByUserId(userId)).thenReturn(taskEntityList);
    }

}
