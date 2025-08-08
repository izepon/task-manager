package com.izepon.task_manager.service;

import com.izepon.task_manager.dto.UserDto.UserCreateRequest;
import com.izepon.task_manager.entity.User;
import com.izepon.task_manager.exception.BusinessRuleException;
import com.izepon.task_manager.exception.ResourceNotFoundException;
import com.izepon.task_manager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final static String USER_TEST = "User test";
    private final static String EMAIL_EXAMPLE = "test@example.com";

    @Test
    void createUser_WhenEmailIsUnique_ShouldCreateUserSuccessfully() {
        var request = new UserCreateRequest(USER_TEST, EMAIL_EXAMPLE);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(request);

        assertNotNull(result);
        assertEquals(request.name(), result.getName());
        assertEquals(request.email(), result.getEmail());
        verify(userRepository, times(1)).existsByEmail(request.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_WhenEmailAlreadyExists_ShouldThrowBusinessRuleException() {
        var request = new UserCreateRequest(USER_TEST, EMAIL_EXAMPLE);
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> {
            userService.createUser(request);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findUserById_WhenUserExists_ShouldReturnUser() {
        var userId = UUID.randomUUID();
        var user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void findUserById_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        var userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findUserById(userId);
        });
    }
}
