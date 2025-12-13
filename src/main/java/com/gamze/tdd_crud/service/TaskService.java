package com.gamze.tdd_crud.service;

import org.springframework.stereotype.Service;

import com.gamze.tdd_crud.entity.Task;
import com.gamze.tdd_crud.repository.TaskRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be null or empty");
        }
        
        return taskRepository.save(task);
    }
}
