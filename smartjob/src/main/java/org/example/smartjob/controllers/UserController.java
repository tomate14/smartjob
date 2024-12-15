package org.example.smartjob.controllers;

import org.example.smartjob.dto.PhoneDTO;
import org.example.smartjob.dto.UserDTO;
import org.example.smartjob.entities.User;
import org.example.smartjob.exceptions.EmailNotExistException;
import org.example.smartjob.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/phones/{id}")
    public ResponseEntity<List<PhoneDTO>> getPhonesUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findPhones(id));
    }
    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody User updatedUser) {
        UserDTO existingUser = userService.updateUser(updatedUser, false);
        return ResponseEntity.ok(existingUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        if (userService.existsById(id)) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new EmailNotExistException("Email is not register");
    }
}
