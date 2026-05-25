package com.example.springboot_assessment.controller;

import com.example.springboot_assessment.model.Task;
import com.example.springboot_assessment.repository.TaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)   // disable security filters while testing
@ActiveProfiles("test") // Component to tell Springboot to ignore default settings and use the one for the "test" environment - see src/test/resources
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;


    private Task task1, task2, task3, task4;

    private List<Task> taskList = new ArrayList<>();

    private static final String API_ENDPOINT = "http://localhost:8080/api/v1/tasks";


    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {

        taskRepository.deleteAll();

        task1 = new Task("Do nothing", false);
        task2 = new Task("Do something", false);
        task3 = new Task("Do nothing and something", false);
        task4 = new Task("Done something", true);

        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);
        taskList.add(task4);

        taskRepository.saveAll(taskList);
    }

    @Test
    @DisplayName("**JUNIT test: get all tasks via TaskController**")
    void getAllTasks() throws Exception {

        ResultActions resultActions = mockMvc.perform(
                get(API_ENDPOINT.concat("/"))
        );

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(taskList.size()));
    }

    @Test
    @DisplayName("**JUNIT test: get all completed tasks via TaskController**")
    void getAllCompletedTasks() throws Exception {

        long completedCount = taskList.stream()
                .filter(task -> task.isCompleted())
                .count();

        ResultActions resultActions = mockMvc.perform(
                get(API_ENDPOINT.concat("/completed"))
        );

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(completedCount));
    }

    @Test
    @DisplayName("**JUNIT test: get all incompleted tasks via TaskController**")
    void getAllIncompleteTasks() throws Exception {

        long notCompletedCount = taskList.stream()
                .filter(task -> !task.isCompleted())
                .count();

        ResultActions resultActions = mockMvc.perform(
                get(API_ENDPOINT.concat("/incomplete"))
        );

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(notCompletedCount));
    }

    @Test
    @DisplayName("**JUNIT test: add task via TaskController**")
    void createTask() throws Exception {

        // arrange  - prepare
        String requestBody = objectMapper.writeValueAsString(task1);

        // act      - action or behavior
        ResultActions resultActions = mockMvc.perform(post(API_ENDPOINT.concat("/"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        // assert   - verify the output
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.task").value(task1.getTask()))
                .andExpect(jsonPath("$.completed").value(task1.isCompleted()));
    }

    @Test
    @DisplayName("**JUNIT test: update task via TaskController**")
    void updateTask() throws Exception {

        // arrange  - prepare
        Long taskId = task2.getId();
        Task curTask = taskRepository.findById(taskId).get();
        curTask.setCompleted(true);
        String requestBody = objectMapper.writeValueAsString(curTask);

        // act      - action or behavior
        ResultActions resultActions = mockMvc.perform(
                put(API_ENDPOINT.concat("/{id}"),taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
        );

        // assert   - verify the output
        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.task").value(curTask.getTask()))
                .andExpect(jsonPath("$.completed").value(curTask.isCompleted()));
    }

    @Test
    @DisplayName("**JUNIT test: delete task via TaskController**")
    void deleteTasks() throws Exception {

        Long taskId = task1.getId();
        Task curTask = taskRepository.findById(taskId).get();
        String requestBody = objectMapper.writeValueAsString(curTask);

        ResultActions resultActions = mockMvc.perform(
                delete(API_ENDPOINT.concat("/{id}"),task1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.task").value(curTask.getTask()))
                .andExpect(jsonPath("$.completed").value(curTask.isCompleted()));

    }
}