package com.enicar.taskManagementApi.Controller;


import com.enicar.taskManagementApi.dto.TaskRequest;
import com.enicar.taskManagementApi.dto.TaskResponse;
import com.enicar.taskManagementApi.Service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        log.info("GET /tasks - Fetching all tasks");
        List<TaskResponse> tasks = taskService.getAllTasks();
        log.info("GET /tasks - Successfully retrieved {} tasks", tasks.size());
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        log.info("POST /tasks - Creating new task with title: '{}'", request.title());
        TaskResponse response = taskService.createTask(request);
        log.info("POST /tasks - Successfully created task with ID: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        log.info("GET /tasks/{} - Fetching task by ID", id);
        try {
            TaskResponse response = taskService.getTaskById(id);
            log.info("GET /tasks/{} - Successfully retrieved task", id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        log.info("PUT /tasks/{} - Updating task", id);
        try {
            TaskResponse response = taskService.updateTask(id, request);
            log.info("PUT /tasks/{} - Successfully updated task", id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("DELETE /tasks/{} - Deleting task", id);
        try {
            taskService.deleteTask(id);
            log.info("DELETE /tasks/{} - Successfully deleted task", id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
