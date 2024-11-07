package com.todo.service.auth;

import com.todo.dto.request.SignupRequest;
import com.todo.dto.response.UserDto;
import com.todo.entity.User;
import com.todo.mapper.UserMapper;
import com.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto signupUser(SignupRequest signupRequest) {
        User user = UserMapper.getUserEntity(signupRequest);
        User savedUser = userRepository.save(user);
        UserDto userDto = UserMapper.getUserDto(savedUser);
        return userDto;
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
