package org.example.smartjob.services.impl;

import org.example.smartjob.dto.PhoneDTO;
import org.example.smartjob.dto.UserDTO;
import org.example.smartjob.entities.Phone;
import org.example.smartjob.entities.User;
import org.example.smartjob.exceptions.EmailNotExistException;
import org.example.smartjob.repositories.UserRepository;
import org.example.smartjob.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDTO createUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.updatePhones();
        User savedUser = userRepository.save(user);
        return UserDTO.buildFromUser(savedUser);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDTO updateUser(User updatedUser, boolean isLogin) {
        User existingUser = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new EmailNotExistException("User not found with ID: " + updatedUser.getId()));

        if (updatedUser.getName() != null && !updatedUser.getName().equals(existingUser.getName())) {
            existingUser.setName(updatedUser.getName());
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
            existingUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPassword() != null && !passwordEncoder.encode(updatedUser.getPassword()).equals(existingUser.getPassword())) {
            existingUser.setPassword(updatedUser.getPassword());
        }

        if (updatedUser.getPhones() != null) {
            updatePhones(existingUser, updatedUser.getPhones());
        }

        if (updatedUser.getToken() != null && !updatedUser.getToken().equals(existingUser.getToken())) {
            existingUser.setToken(updatedUser.getToken());
        }
        if (!isLogin) {
            existingUser.setModified(LocalDateTime.now());
        }

        User savedUser = userRepository.save(existingUser);

        return UserDTO.buildFromUser(savedUser);
    }


    @Override
    public UserDTO findById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EmailNotExistException("User not exist");
        }
        return UserDTO.buildFromUser(user.get());
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDTO::buildFromUser).toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<PhoneDTO> findPhones(UUID id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> value
                .getPhones()
                .stream()
                .map(PhoneDTO::buildFromPhone)
                .toList()).orElseGet(List::of);
    }


    private void updatePhones(User existingUser, List<Phone> updatedPhones) {
        existingUser.getPhones().removeIf(phone ->
                updatedPhones.stream().noneMatch(updatedPhone -> updatedPhone.getId() != null && updatedPhone.getId().equals(phone.getId()))
        );

        for (Phone updatedPhone : updatedPhones) {
            if (updatedPhone.getId() == null) {
                updatedPhone.setUser(existingUser);
                existingUser.getPhones().add(updatedPhone);
            } else {
                existingUser.getPhones().stream()
                        .filter(phone -> phone.getId().equals(updatedPhone.getId()))
                        .findFirst()
                        .ifPresent(phone -> {
                            phone.setNumber(updatedPhone.getNumber());
                            phone.setCityCode(updatedPhone.getCityCode());
                            phone.setCountryCode(updatedPhone.getCountryCode());
                        });
            }
        }
    }

}