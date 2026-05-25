package com.example.springboot_assessment.controller;


import com.example.springboot_assessment.exception.ResourceNotFoundException;
import com.example.springboot_assessment.model.Task;
import com.example.springboot_assessment.service.TaskServiceInterface;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/tasks")
public class TaskController {

    TaskServiceInterface taskService;

    public TaskController(TaskServiceInterface taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.getAllTask(), HttpStatus.OK);
    }
    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getAllCompletedTasks() {
        return new ResponseEntity<>(taskService.findAllCompletedTask(), HttpStatus.OK);
    }
    @GetMapping("/incomplete")
    public ResponseEntity<List<Task>> getAllIncompleteTasks() {
        return new ResponseEntity<>(taskService.findAllInCompleteTask(), HttpStatus.OK);
    }
    @PostMapping("/")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        return new ResponseEntity<>(taskService.createNewTask(task), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task task) throws ResourceNotFoundException {

        Task curTask = taskService.findTaskById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Task id %d does not exist.", id))
        );
        return new ResponseEntity<>(taskService.updateTask(task), HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteTasks(@PathVariable Long id) throws ResourceNotFoundException {

        Task curTask = taskService.findTaskById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Task id %d does not exist.", id))
        );
        taskService.deleteTask(curTask);
        return new ResponseEntity<>(curTask, HttpStatus.OK);
    }
}
