package com.todo.service.task;

import com.todo.dto.request.TaskRequest;
import com.todo.dto.response.TaskDto;
import com.todo.enums.TaskStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {

    TaskDto createTask(TaskRequest taskRequest);

    List<TaskDto> getTasksForConnectedUser();

    List<TaskDto> getTasksByUserId(Long id);

    void deleteTask(Long id);

    TaskDto getTaskById(Long id);

    TaskDto updateTask(Long id,TaskDto taskDto);

    TaskDto updateTaskStatus(Long taskId, TaskStatus status);

    Page<TaskDto> getAllTasks(int page, int size);

}
