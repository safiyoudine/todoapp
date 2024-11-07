package com.todo.service.task;

import com.todo.dto.request.TaskRequest;
import com.todo.dto.request.UpdateTaskRequest;
import com.todo.dto.response.TaskDto;
import com.todo.entity.Category;
import com.todo.entity.Task;
import com.todo.entity.User;
import com.todo.enums.TaskStatus;
import com.todo.exception.TaskNotFoundException;
import com.todo.mapper.TaskMapper;
import com.todo.repository.CategoryRepository;
import com.todo.repository.TaskRepository;
import com.todo.repository.UserRepository;
import com.todo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public TaskDto createTask(TaskRequest taskRequest) {
        User currentUser = userService.getAuthenticatedUsername();
        Optional<Category> category = categoryRepository.findById(taskRequest.getCategoryId());

        if (currentUser != null) {
            Task task = TaskMapper.getTaskEntity(taskRequest);
            task.setTaskStatus(TaskStatus.TODO);
            task.setUser(currentUser);
            task.setCategory(category.get());
            Task savedTask = taskRepository.save(task);
            return TaskMapper.getTaskDto(savedTask);
        }
        return null;
    }

    @Override
    public List<TaskDto> getTasksForConnectedUser() {
        User currentUser = userService.getAuthenticatedUsername();
        List<Task> tasks = taskRepository.findAllByUserId(currentUser.getId());

        if (tasks == null || tasks.isEmpty()) {
            return Collections.emptyList();
        }
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(TaskMapper::getTaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDto> getTasksByUserId(Long id) {
        List<Task> tasks = taskRepository.findAllByUserId(id);

        if (tasks == null || tasks.isEmpty()) {
            return Collections.emptyList();
        }
        return tasks.stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(TaskMapper::getTaskDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            return optionalTask.map(TaskMapper::getTaskDto).orElseThrow(() -> new TaskNotFoundException(id));
        }
        throw new TaskNotFoundException(id);
    }

    @Override
    public TaskDto updateTask(Long id, UpdateTaskRequest updateTaskRequest) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent() ) {
            Task existingTask = optionalTask.get();

            if (updateTaskRequest.getTitle() != null) {
                existingTask.setTitle(updateTaskRequest.getTitle());
            }
            if (updateTaskRequest.getDescription() != null) {
                existingTask.setDescription(updateTaskRequest.getDescription());
            }
            if (updateTaskRequest.getDueDate() != null) {
                existingTask.setDueDate(updateTaskRequest.getDueDate());
            }
            if (updateTaskRequest.getTaskStatus() != null) {
                existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(updateTaskRequest.getTaskStatus())));
            }

            Task updatedTask = taskRepository.save(existingTask);
            return TaskMapper.getTaskDto(updatedTask);
        }

        return null;
    }

    private TaskStatus mapStringToTaskStatus(String status) {
        return switch (status) {
            case "TODO" -> TaskStatus.TODO;
            case "INPROGRESS" -> TaskStatus.INPROGRESS;
            case "COMPLETED" -> TaskStatus.COMPLETED;
            default -> TaskStatus.CANCELD;
        };
    }

    @Override
    public TaskDto updateTaskStatus(Long taskId, TaskStatus status) {
        return taskRepository.findById(taskId).map(task -> {
            task.setTaskStatus(status);
            Task updatedTask = taskRepository.save(task);
            return TaskMapper.getTaskDto(updatedTask);
        }).orElse(null);
    }


    @Override
    public Page<TaskDto> getAllTasks(int page, int size) {
        User currentUser = userService.getAuthenticatedUsername();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dueDate"));
        Page<Task> tasksPage = taskRepository.findAllByUserId(currentUser.getId(), pageable);
        return tasksPage.map(TaskMapper::getTaskDto);
    }
}
