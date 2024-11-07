package com.todo.mapper;

import com.todo.dto.request.TaskRequest;
import com.todo.dto.response.TaskDto;
import com.todo.entity.Task;


public class TaskMapper {

    public static Task getTaskEntity(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        return task;
    }

    public static TaskDto getTaskDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        taskDto.setDueDate(task.getDueDate());
        taskDto.setUserId(task.getUser().getId());
        taskDto.setName(task.getUser().getFirstname() + " " + task.getUser().getLastname());
        taskDto.setCategoryId(task.getCategory().getId());
        taskDto.setCategoryLabel(task.getCategory().getLabel());
        taskDto.setTaskStatus(task.getTaskStatus());
        return taskDto;

    }
}
