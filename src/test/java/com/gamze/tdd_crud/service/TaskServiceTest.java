package com.gamze.tdd_crud.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gamze.tdd_crud.entity.Task;
import com.gamze.tdd_crud.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void shouldCreateTaskSuccessfully() {
        Task task = new Task();
        task.setTitle("Finish the project");
        task.setDescription("Complete the TDD CRUD application by end of the week");
        task.setDueDate("2024-12-14");
        task.setCompleted(false);

        given(taskRepository.save(any(Task.class))).willReturn(task);

        Task savedTask = taskService.createTask(task);

        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Finish the project");
        assertThat(savedTask.getDescription()).isEqualTo("Complete the TDD CRUD application by end of the week");
        assertThat(savedTask.getDueDate()).isEqualTo("2024-12-14");
        assertThat(savedTask.isCompleted()).isFalse();
        assertThat(savedTask.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull() {
        Task task = new Task();
        task.setTitle(null);

        assertThatThrownBy(() -> taskService.createTask(task))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Task title cannot be null or empty");
    }

    @Test 
    void shouldReadTaskSuccessfully() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Read the book");

        given(taskRepository.findById(1L)).willReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);
        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getId()).isEqualTo(1L);
        assertThat(foundTask.getTitle()).isEqualTo("Read the book");
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        given(taskRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Task not found with id: 1");
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setDueDate("2024-12-14");
        existingTask.setCompleted(true);

        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");
        updatedTask.setDescription("New Description");
        updatedTask.setDueDate("2024-12-20");
        updatedTask.setCompleted(true);

        given(taskRepository.findById(1L)).willReturn(Optional.of(existingTask));
        given(taskRepository.save(any(Task.class))).willReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getDescription()).isEqualTo("New Description");
        assertThat(result.isCompleted()).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTask() {
        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");

        given(taskRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(1L, updatedTask))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Task not found with id: 1");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithNullTitle() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");

        Task updatedTask = new Task();
        updatedTask.setTitle(null);

        assertThatThrownBy(() -> taskService.updateTask(1L, updatedTask))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Task title cannot be null or empty");
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Finish the project");

        given(taskRepository.findById(1L)).willReturn(Optional.of(task));
        
        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).delete(task);
    } 

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        given(taskRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Task not found with id: 1");
    }
}
