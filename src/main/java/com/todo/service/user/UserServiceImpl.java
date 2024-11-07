package com.todo.service.user;

import com.todo.dto.response.UserDto;
import com.todo.entity.User;
import com.todo.mapper.UserMapper;
import com.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::getUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public User getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            return userRepository.findByEmail(authentication.getPrincipal().toString());
        }
        return null;
    }

}
