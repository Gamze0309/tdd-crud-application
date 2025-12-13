package com.gamze.tdd_crud.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gamze.tdd_crud.entity.Task;
import com.gamze.tdd_crud.service.TaskService;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldCreateTask() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("New Task");

        given(taskService.createTask(any(Task.class))).willReturn(task);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Task\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    @Test
    void shouldReturn400WhenTitleIsNull() throws Exception {
        given(taskService.createTask(any(Task.class)))
                .willThrow(new IllegalArgumentException("Task title cannot be null or empty"));

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReadTask() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("New Task");

        given(taskService.getTaskById(1L)).willReturn(task);

        mockMvc.perform(get("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        given(taskService.getTaskById(1L))
                .willThrow(new RuntimeException("Task not found with id: 1"));

        mockMvc.perform(get("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
