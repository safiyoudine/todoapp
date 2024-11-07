package com.todo.controller.task;

import com.todo.dto.request.StatusUpdateRequest;
import com.todo.dto.request.TaskRequest;
import com.todo.dto.request.UpdateTaskRequest;
import com.todo.dto.response.TaskDto;
import com.todo.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/task")
    public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest) {
        TaskDto createdTask = taskService.createTask(taskRequest);
        if (createdTask == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @GetMapping("/tasks/user")
    public ResponseEntity<?> getTasksForConnectedUser() {
        List<TaskDto> taskById = taskService.getTasksForConnectedUser();
        return new ResponseEntity<>(taskById, HttpStatus.OK);
    }


    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<TaskDto> pagedResult = taskService.getAllTasks(page, size);
        return ResponseEntity.ok(pagedResult);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<TaskDto> getTaskByTaskId(@PathVariable("taskId") Long taskId) {
        TaskDto taskDto = taskService.getTaskById(taskId);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    @PutMapping("/task/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable("taskId") Long taskId, @RequestBody UpdateTaskRequest updateTaskRequest) {
        TaskDto updateTask = taskService.updateTask(taskId, updateTaskRequest);
        if (updateTask == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(updateTask, HttpStatus.OK);
    }

    @PutMapping("/task/{taskId}/status")
    public ResponseEntity<TaskDto> updateTaskStatus(@PathVariable Long taskId,
                                                    @RequestBody StatusUpdateRequest statusUpdateRequest) {
        TaskDto updatedTask = taskService.updateTaskStatus(taskId, statusUpdateRequest.getStatus());
        if (updatedTask != null) {
            return ResponseEntity.ok(updatedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
