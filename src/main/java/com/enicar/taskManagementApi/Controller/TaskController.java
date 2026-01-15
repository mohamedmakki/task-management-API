package com.enicar.taskManagementApi.Controller;


import com.enicar.taskManagementApi.dto.TaskRequest;
import com.enicar.taskManagementApi.dto.TaskResponse;
import com.enicar.taskManagementApi.Service.TaskService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    
    // Metrics
    private final Counter taskCreateCounter;
    private final Counter taskRetrievalCounter;
    private final Counter taskDeletionCounter;
    private final Timer taskRetrievalTimer;

    public TaskController(TaskService taskService, MeterRegistry meterRegistry) {
        this.taskService = taskService;
        
        this.taskCreateCounter = Counter.builder("tasks.create.request")
                .description("Total number of task creation requests")
                .register(meterRegistry);
        this.taskRetrievalCounter = Counter.builder("tasks.retrieval.total")
                .description("Total number of task retrieval requests")
                .register(meterRegistry);
        this.taskDeletionCounter = Counter.builder("tasks.deletion.request")
                .description("Total number of task deletion requests")
                .register(meterRegistry);
        this.taskRetrievalTimer = Timer.builder("tasks.retrieval.timer")
                .description("Time taken to retrieve tasks")
                .register(meterRegistry);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        log.info("GET /tasks - Fetching all tasks");
        return taskRetrievalTimer.record(() -> {
            taskRetrievalCounter.increment();
            List<TaskResponse> tasks = taskService.getAllTasks();
            log.info("GET /tasks - Successfully retrieved {} tasks", tasks.size());
            return ResponseEntity.ok(tasks);
        });
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        log.info("POST /tasks - Creating new task with title: '{}'", request.title());
        taskCreateCounter.increment();
        TaskResponse response = taskService.createTask(request);
        log.info("POST /tasks - Successfully created task with ID: {}", response.id());
        URI location = URI.create(String.format("/tasks/%s", response.id()));
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        log.info("GET /tasks/{} - Fetching task by ID", id);
        return taskRetrievalTimer.record(() -> {
            taskRetrievalCounter.increment();
            try {
                TaskResponse response = taskService.getTaskById(id);
                log.info("GET /tasks/{} - Successfully retrieved task", id);
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                log.error("GET /tasks/{} - Task not found: {}", id, e.getMessage());
                return ResponseEntity.notFound().build();
            }
        });
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
            log.warn("PUT /tasks/{} - Failed to update: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("DELETE /tasks/{} - Deleting task", id);
        taskDeletionCounter.increment();
        try {
            taskService.deleteTask(id);
            log.info("DELETE /tasks/{} - Successfully deleted task", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.warn("DELETE /tasks/{} - Failed to delete: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
