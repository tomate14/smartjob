package org.example.smartjob.services;

import org.example.smartjob.dto.UserDTO;
import org.example.smartjob.entities.User;

public interface AuthService {
    UserDTO login(User user);
    UserDTO register(User user);
}
