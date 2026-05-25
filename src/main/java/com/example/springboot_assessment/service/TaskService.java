package com.example.springboot_assessment.service;


import com.example.springboot_assessment.model.Task;
import com.example.springboot_assessment.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService implements TaskServiceInterface {

    //Properties
    TaskRepository taskRepository;

    //Constructors
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //Methods
    @Override
    public Task createNewTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findAllCompletedTask() {
        return taskRepository.findByCompletedTrue();
    }

    @Override
    public List<Task> findAllInCompleteTask() {
        return taskRepository.findByCompletedFalse();
    }

    @Override
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }
}


