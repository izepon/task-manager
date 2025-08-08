package com.izepon.task_manager.service;

import com.izepon.task_manager.dto.UserDto.UserCreateRequest;
import com.izepon.task_manager.entity.User;
import com.izepon.task_manager.exception.BusinessRuleException;
import com.izepon.task_manager.exception.ResourceNotFoundException;
import com.izepon.task_manager.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException("Email already in use.");
        }
        User newUser = new User();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        return userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }
}
