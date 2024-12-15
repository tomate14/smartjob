package org.example.smartjob.controllers;

import org.example.smartjob.dto.UserDTO;
import org.example.smartjob.entities.User;
import org.example.smartjob.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        UserDTO savedUser = authService.login(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        UserDTO savedUser = authService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}


