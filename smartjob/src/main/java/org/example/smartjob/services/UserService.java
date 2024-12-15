package org.example.smartjob.services;

import org.example.smartjob.dto.PhoneDTO;
import org.example.smartjob.dto.UserDTO;
import org.example.smartjob.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO createUser(User user);
    User findByEmail(String email);
    UserDTO updateUser(User existingUser, boolean isLogin);

    UserDTO findById(UUID id);

    List<UserDTO> findAll();

    boolean existsById(UUID id);

    void deleteById(UUID id);

    List<PhoneDTO> findPhones(UUID id);
}
