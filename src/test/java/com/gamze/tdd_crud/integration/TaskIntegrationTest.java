package com.gamze.tdd_crud.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.gamze.tdd_crud.entity.Task;
import com.jayway.jsonpath.JsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAndReadTask() throws Exception {
        Task createdTask = new Task();
        createdTask.setTitle("Integration Test Task");
        createdTask.setDescription("Integration Test Description");
        createdTask.setDueDate("2024-12-20");

        String taskJson = "{\"title\":\"" + createdTask.getTitle() + "\", \"description\":\"" + createdTask.getDescription() + "\", \"dueDate\":\"" + createdTask.getDueDate() + "\"}";
        String response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andExpect(jsonPath("$.description").value("Integration Test Description"))
                .andExpect(jsonPath("$.dueDate").value("2024-12-20"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer id = JsonPath.read(response, "$.id");
        Long createdTaskId = id.longValue();
        
        mockMvc.perform(get("/api/tasks/" + createdTaskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdTaskId))
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andExpect(jsonPath("$.description").value("Integration Test Description"))
                .andExpect(jsonPath("$.dueDate").value("2024-12-20"));
    }

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateTask() throws Exception {
        String taskJson = "{\"title\":\"Original Title\", \"description\":\"Original Description\", \"dueDate\":\"2024-12-20\"}";
        String response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Integer id = JsonPath.read(response, "$.id");
        Long taskId = id.longValue();

        String updatedTaskJson = "{\"title\":\"Updated Title\", \"description\":\"Updated Description\", \"dueDate\":\"2024-12-25\"}";
        mockMvc.perform(put("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.dueDate").value("2024-12-25"));
    }
}
