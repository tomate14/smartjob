package org.example.smartjob;

import org.example.smartjob.controllers.AuthController;
import org.example.smartjob.dto.UserDTO;
import org.example.smartjob.entities.User;
import org.example.smartjob.exceptions.EmailAlreadyExistException;
import org.example.smartjob.exceptions.EmailNotExistException;
import org.example.smartjob.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("Test1234");
        user.setName("Test User");
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setCreated(LocalDateTime.now());
        user.setActive(true);
        user.setToken("mocktoken");
        userDTO = UserDTO.buildFromUser(user);
    }

    @Test
    void loginSuccess() {
        when(authService.login(any(User.class))).thenReturn(userDTO);

        ResponseEntity<?> response = authController.login(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void loginUserNotFound() {
        when(authService.login(any(User.class))).thenThrow(new EmailNotExistException("Email is not register"));
        try {
            authController.login(user);
        } catch (Exception e) {
            assertThrows(EmailNotExistException.class, () -> authService.login(user));
        }
    }

    @Test
    void registerSuccess() {
        when(authService.register(any(User.class))).thenReturn(userDTO);

        ResponseEntity<?> response = authController.register(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void registerEmailAlreadyExists() {
        when(authService.register(any(User.class))).thenThrow(new EmailAlreadyExistException("Email already exists"));
        try {
            authController.register(user);
        } catch (Exception e) {
            assertThrows(EmailAlreadyExistException.class, () -> authService.register(user));
        }

    }

    @Test
    void registerEmailNotValid() {
        when(authService.register(any(User.class))).thenThrow(new EmailNotExistException("Email is not valid"));
        try {
            authController.register(user);
        } catch (Exception e) {
            assertThrows(EmailNotExistException.class, () -> authService.register(user));
        }
    }

    @Test
    void registerPasswordNotValid() {
        when(authService.register(any(User.class))).thenThrow(new EmailNotExistException("Password is not valid"));

        try {
            authController.register(user);
        } catch (Exception e) {
            assertThrows(EmailNotExistException.class, () -> authService.register(user));
        }
    }
}
