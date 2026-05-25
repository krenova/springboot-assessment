package com.example.springboot_assessment.service;

import com.example.springboot_assessment.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskServiceInterface {

    // Method signatures for TaskService
    public Task createNewTask(Task task);

    public List<Task> getAllTask();

    public Optional<Task> findTaskById(Long id);

    public List<Task> findAllCompletedTask();

    public List<Task> findAllInCompleteTask();

    public void deleteTask(Task task);

    public Task updateTask(Task task);
}
