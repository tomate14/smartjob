package org.example.smartjob.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.smartjob.entities.User;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO extends User {
    public UserDTO() {
    }

    public UserDTO(UUID id, LocalDateTime created, LocalDateTime modified, LocalDateTime lastLogin, String token, boolean isActive) {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.lastLogin = lastLogin;
        this.token = token;
        this.isActive = isActive;
    }

    public static UserDTO buildFromUser(User user) {
        return new UserDTO(user.getId(), user.getCreated(), user.getModified(), user.getLastLogin(), user.getToken(), user.isActive());
    }

}
