package com.todo.service.user;

import com.todo.dto.response.UserDto;
import com.todo.entity.User;

import java.util.List;

public interface UserService{

    List<UserDto> getUsers();

    User getAuthenticatedUsername();

}
