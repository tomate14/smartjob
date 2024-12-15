package org.example.smartjob;

import org.example.smartjob.controllers.UserController;
import org.example.smartjob.dto.PhoneDTO;
import org.example.smartjob.dto.UserDTO;
import org.example.smartjob.entities.User;
import org.example.smartjob.exceptions.EmailNotExistException;
import org.example.smartjob.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UUID userId;
    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        userDTO = new UserDTO();
        user = new User();
    }

    @Test
    void testGetAllUsers() {
        when(userService.findAll()).thenReturn(List.of(userDTO));

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        when(userService.findById(userId)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        assertNotNull(response);
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).findById(userId);
    }

    @Test
    void testGetPhonesUserById() {
        List<PhoneDTO> phones = List.of(new PhoneDTO("123456789", "123", "1"));
        when(userService.findPhones(userId)).thenReturn(phones);

        ResponseEntity<List<PhoneDTO>> response = userController.getPhonesUserById(userId);

        assertNotNull(response);
        assertEquals(phones.size(), response.getBody().size());
        verify(userService, times(1)).findPhones(userId);
    }

    @Test
    void testUpdateUser() {
        when(userService.updateUser(user, false)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.updateUser(user);

        assertNotNull(response);
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).updateUser(user, false);
    }

    @Test
    void testDeleteUser() {
        when(userService.existsById(userId)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
        verify(userService, times(1)).existsById(userId);
        verify(userService, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUserThrowsException() {
        when(userService.existsById(userId)).thenReturn(false);

        EmailNotExistException exception = assertThrows(EmailNotExistException.class, () -> userController.deleteUser(userId));

        assertEquals("Email is not register", exception.getMessage());
        verify(userService, times(1)).existsById(userId);
        verify(userService, never()).deleteById(any());
    }
}
