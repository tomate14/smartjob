package org.example.smartjob.services.impl;

import org.example.smartjob.dto.UserDTO;
import org.example.smartjob.entities.User;
import org.example.smartjob.exceptions.EmailAlreadyExistException;
import org.example.smartjob.exceptions.EmailNotExistException;
import org.example.smartjob.security.CustomUserDetailsService;
import org.example.smartjob.security.JwtService;
import org.example.smartjob.services.AuthService;
import org.example.smartjob.services.UserService;
import org.example.smartjob.validator.EmailValidator;
import org.example.smartjob.validator.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordValidator passwordValidator;

    public UserDTO login(User user) {
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser == null) {
            throw new EmailNotExistException("Email is not register");
        }
        //Authenticate to check email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

        String token = jwtService.generateToken(userDetails, existingUser.getToken());
        existingUser.setToken(token);
        existingUser.setLastLogin(LocalDateTime.now());
        return userService.updateUser(existingUser, true);
    }

    public UserDTO register(User user) {
        User existingUser = userService.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new EmailAlreadyExistException("Email already exists");
        }
        if (!EmailValidator.isValid(user)) {
            throw new EmailNotExistException("Email is not valid");
        }

        if (!passwordValidator.isValid(user.getPassword())) {
            throw new BadCredentialsException("Password is not valid");
        }

        // Registration flow
        user.setId(UUID.randomUUID());
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);

        String token = jwtService.createToken(new HashMap<>(), user.getEmail());
        user.setToken(token);
        // Persist user
        return userService.createUser(user);
    }
}
